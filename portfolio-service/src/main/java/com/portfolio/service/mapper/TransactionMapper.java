package com.portfolio.service.mapper;

import com.portfolio.service.api.model.TransactionResponse;
import com.portfolio.service.api.model.Type;
import com.portfolio.service.persistence.model.TransactionEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionMapper {

    public static TransactionResponse mapToResponse (TransactionEntity transactionEntity) {

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transactionEntity.getId());
        transactionResponse.setSymbol(transactionEntity.getHoldingEntity().getSymbol());
        transactionResponse.setType(Type.valueOf(transactionEntity.getTransactionType().name()));
        transactionResponse.setQuantity(transactionEntity.getQuantity());
        transactionResponse.setPricePerShare(transactionEntity.getPricePerShare());
        transactionResponse.setExecutedAt(transactionEntity.getExecutedAt().atOffset(ZoneOffset.UTC));

        return transactionResponse;
    }
}
