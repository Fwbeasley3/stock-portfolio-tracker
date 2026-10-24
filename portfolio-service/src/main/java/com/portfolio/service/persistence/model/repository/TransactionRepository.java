package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.TransactionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  @EntityGraph(attributePaths = "holdingEntity")
  List<TransactionEntity> findByHoldingEntity_PortfolioEntity_IdOrderByExecutedAtDesc(
      Long portfolioId);
}
