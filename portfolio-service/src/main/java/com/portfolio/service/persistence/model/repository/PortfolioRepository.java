package com.portfolio.service.persistence.model.repository;

import com.portfolio.service.persistence.model.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    List<PortfolioEntity> findByUserEntity_Id(Long userId);

    Optional<PortfolioEntity> findByIdAndUserEntity_Id(Long portfolioId, Long userId);


}
