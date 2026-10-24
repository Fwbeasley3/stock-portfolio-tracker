package com.portfolio.service.mapper;

import com.portfolio.service.api.model.PortfolioResponse;
import com.portfolio.service.persistence.model.PortfolioEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PortfolioMapper {

  public static PortfolioResponse mapToResponse(PortfolioEntity portfolioEntity) {

    PortfolioResponse portfolioResponse = new PortfolioResponse();
    portfolioResponse.setId(portfolioEntity.getId());
    portfolioResponse.setName(portfolioEntity.getName());
    portfolioResponse.setCreatedAt(portfolioEntity.getCreatedAt().atOffset(ZoneOffset.UTC));
    portfolioResponse.setUpdatedAt(portfolioEntity.getUpdatedAt().atOffset(ZoneOffset.UTC));

    return portfolioResponse;
  }
}
