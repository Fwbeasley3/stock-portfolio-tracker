package com.portfolio.service.api.delegate;

import com.portfolio.service.api.TransactionsApiDelegate;
import com.portfolio.service.api.model.CreateTransactionRequest;
import com.portfolio.service.api.model.TransactionResponse;
import com.portfolio.service.domain.model.TransactionType;
import com.portfolio.service.mapper.TransactionMapper;
import com.portfolio.service.persistence.model.TransactionEntity;
import com.portfolio.service.persistence.model.UserEntity;
import com.portfolio.service.service.TransactionService;
import com.portfolio.service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionApiDelegateImpl implements TransactionsApiDelegate {

    private final UserService userService;
    private final TransactionService transactionService;
    @Override
    public ResponseEntity<TransactionResponse> createTransaction(Long portfolioId, CreateTransactionRequest createTransactionRequest) {

        // TODO Replace development identity with authenticated JWT subject
        String testExternalAuthId = "dev-user-123";
        String symbol = createTransactionRequest.getSymbol();
        TransactionType transactionType = TransactionType.valueOf(createTransactionRequest.getType().name());
        BigDecimal quantity = createTransactionRequest.getQuantity();
        BigDecimal pricePerShare = createTransactionRequest.getPricePerShare();

        UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

        TransactionEntity transactionEntity = transactionService.createTransaction(portfolioId, userEntity.getId(), symbol, transactionType, quantity, pricePerShare);

        TransactionResponse transactionResponse = TransactionMapper.mapToResponse(transactionEntity);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionResponse);
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getTransactions(Long portfolioId) {

        // TODO Replace development identity with authenticated JWT subject
        String testExternalAuthId = "dev-user-123";
        UserEntity userEntity = userService.findOrCreateUser(testExternalAuthId);

        List<TransactionEntity> transactionEntityList = transactionService.getTransactions(portfolioId, userEntity.getId());

        List<TransactionResponse> responseEntityList = transactionEntityList.stream().map(TransactionMapper::mapToResponse).toList();

        return ResponseEntity.ok(responseEntityList);
    }
}
