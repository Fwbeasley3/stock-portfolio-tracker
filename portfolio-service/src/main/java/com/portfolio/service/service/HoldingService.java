package com.portfolio.service.service;

import com.portfolio.service.exception.HoldingAlreadyExistsException;
import com.portfolio.service.exception.HoldingNotFoundException;
import com.portfolio.service.persistence.model.HoldingEntity;
import com.portfolio.service.persistence.model.PortfolioEntity;
import com.portfolio.service.persistence.model.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HoldingService {

    private final HoldingRepository holdingRepository;
    private final PortfolioService portfolioService;

    @Transactional
    public HoldingEntity createHolding(Long portfolioId, Long userId, String symbol, BigDecimal quantity, BigDecimal averageCost) {

        // Check if portfolio exists
        PortfolioEntity portfolioEntity = portfolioService.getPortfolio(portfolioId, userId);

        String symbolUpperCase = symbol.trim().toUpperCase(Locale.ROOT);
        // Check the holding doesn't already exist in the portfolio
        boolean exists = holdingRepository.existsByPortfolioEntity_IdAndSymbol(portfolioId, symbolUpperCase);

       if (exists) {
           throw new HoldingAlreadyExistsException("Holding Already exists");
       }

       HoldingEntity holdingEntity = new HoldingEntity(portfolioEntity,symbolUpperCase, quantity,averageCost);

       return holdingRepository.save(holdingEntity);
    }

    @Transactional(readOnly = true)
    public HoldingEntity getHolding (Long portfolioId, Long holdingId) {

        return holdingRepository.findByPortfolioEntity_IdAndId(portfolioId, holdingId).orElseThrow(() -> new HoldingNotFoundException("Holding not found"));
    }

    @Transactional(readOnly = true)
    public List<HoldingEntity> getHoldings (Long portfolioId) {

        return holdingRepository.findByPortfolioEntity_Id(portfolioId);
    }

    @Transactional(readOnly = true)
    public Optional<HoldingEntity> findHoldingBySymbol (Long portfolioId, String symbol) {

        return holdingRepository.findByPortfolioEntity_IdAndSymbol(portfolioId, symbol);
    }
}
