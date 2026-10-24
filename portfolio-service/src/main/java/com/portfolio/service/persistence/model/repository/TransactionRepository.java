package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByHoldingEntity_IdOrderByExecutedAtDesc(Long holdingId);
}
