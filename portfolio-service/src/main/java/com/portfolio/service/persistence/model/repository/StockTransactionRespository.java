package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.StockTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransactionRespository extends JpaRepository<StockTransactionEntity, Long> {

    List<StockTransactionEntity> findByHoldingEntity_IdOrderByExecutedAtDesc(Long holdingId);
}
