package com.portfolio.service.service;

import com.portfolio.service.domain.model.TransactionType;
import com.portfolio.service.exception.HoldingNotFoundException;
import com.portfolio.service.exception.InsufficientSharesException;
import com.portfolio.service.persistence.model.HoldingEntity;
import com.portfolio.service.persistence.model.TransactionEntity;
import com.portfolio.service.persistence.model.repository.TransactionRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final PortfolioService portfolioService;
  private final HoldingService holdingService;

  @Transactional
  public TransactionEntity createTransaction(
      Long portfolioId,
      Long userId,
      String symbol,
      TransactionType type,
      BigDecimal quantity,
      BigDecimal pricePerShare) {

    // Validate transaction
    validateTransaction(symbol, type, quantity, pricePerShare);

    // Check if portfolio exists
    portfolioService.getPortfolio(portfolioId, userId);

    String symbolUpperCase = symbol.trim().toUpperCase(Locale.ROOT);

    Optional<HoldingEntity> holdingEntityOpt =
        holdingService.findHoldingBySymbol(portfolioId, symbolUpperCase);

    HoldingEntity holdingEntity;

    if (type == TransactionType.BUY) {

      holdingEntity =
          processBuyTransaction(
              holdingEntityOpt, portfolioId, userId, symbolUpperCase, quantity, pricePerShare);

    } else if (type == TransactionType.SELL) {

      holdingEntity = processSellTransaction(holdingEntityOpt, quantity);
    } else {
      throw new IllegalArgumentException("Invalid transaction type");
    }

    TransactionEntity transactionEntity =
        new TransactionEntity(holdingEntity, type, quantity, pricePerShare);
    return transactionRepository.save(transactionEntity);
  }

  @Transactional(readOnly = true)
  public List<TransactionEntity> getTransactions(Long portfolioId, Long userId) {

    // Validate portfolio belongs to the user
    portfolioService.getPortfolio(portfolioId, userId);

    return transactionRepository.findByHoldingEntity_PortfolioEntity_IdOrderByExecutedAtDesc(
        portfolioId);
  }

  private void validateTransaction(
      String symbol, TransactionType type, BigDecimal quantity, BigDecimal pricePerShare) {

    if (StringUtils.isBlank(symbol)) {
      throw new IllegalArgumentException("Symbol is null or blank");
    }

    if (type == null) {
      throw new IllegalArgumentException("Transaction type is required");
    }

    if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Quantity is null or less than 0");
    }

    if (pricePerShare == null || pricePerShare.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Price per share is null or less than 0");
    }
  }

  private HoldingEntity processBuyTransaction(
      Optional<HoldingEntity> holdingEntityOpt,
      Long portfolioId,
      Long userId,
      String symbolUpperCase,
      BigDecimal quantity,
      BigDecimal pricePerShare) {

    if (holdingEntityOpt.isEmpty()) {
      return holdingService.createHolding(
          portfolioId, userId, symbolUpperCase, quantity, pricePerShare);
    }
    HoldingEntity holdingEntity = holdingEntityOpt.get();

    BigDecimal currentQuantity = holdingEntity.getQuantity();
    BigDecimal newQuantity = currentQuantity.add(quantity);

    BigDecimal existingCost = currentQuantity.multiply(holdingEntity.getAverageCost());
    BigDecimal purchaseCost = quantity.multiply(pricePerShare);
    BigDecimal totalCost = existingCost.add(purchaseCost);

    BigDecimal newAverageCost = totalCost.divide(newQuantity, 4, RoundingMode.HALF_UP);

    holdingEntity.setQuantity(newQuantity);
    holdingEntity.setAverageCost(newAverageCost);

    return holdingEntity;
  }

  private HoldingEntity processSellTransaction(
      Optional<HoldingEntity> holdingEntityOpt, BigDecimal quantity) {
    if (holdingEntityOpt.isEmpty()) {
      throw new HoldingNotFoundException("Holding not found");
    }
    HoldingEntity holdingEntity = holdingEntityOpt.get();
    BigDecimal currentQuantity = holdingEntity.getQuantity();
    if (currentQuantity.compareTo(quantity) < 0) {
      throw new InsufficientSharesException(
          "Current quantity of shares is less than requested shares to sell");
    }
    BigDecimal remainingQuantity = currentQuantity.subtract(quantity);
    holdingEntity.setQuantity(remainingQuantity);

    return holdingEntity;
  }
}
