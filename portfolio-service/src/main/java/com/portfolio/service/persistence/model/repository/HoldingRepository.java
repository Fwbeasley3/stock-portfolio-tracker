package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.HoldingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<HoldingEntity, Long> {

  List<HoldingEntity> findByPortfolioEntity_Id(Long portfolioId);

  Optional<HoldingEntity> findByPortfolioEntity_IdAndSymbol(Long portfolioId, String symbol);

  Optional<HoldingEntity> findByPortfolioEntity_IdAndId(Long portfolioId, Long holdingId);

  boolean existsByPortfolioEntity_IdAndSymbol(Long portfolioId, String symbol);
}
