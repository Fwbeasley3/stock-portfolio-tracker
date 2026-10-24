package com.portfolio.service.mapper;

import com.portfolio.service.api.model.HoldingResponse;
import com.portfolio.service.persistence.model.HoldingEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HoldingMapper {

  public static HoldingResponse mapToResponse(HoldingEntity holdingEntity) {

    HoldingResponse holdingResponse = new HoldingResponse();
    holdingResponse.setId(holdingEntity.getId());
    holdingResponse.setSymbol(holdingEntity.getSymbol());
    holdingResponse.setQuantity(holdingEntity.getQuantity());
    holdingResponse.setAverageCost(holdingEntity.getAverageCost());
    holdingResponse.setCreatedAt(holdingEntity.getCreatedAt().atOffset(ZoneOffset.UTC));
    holdingResponse.setUpdatedAt(holdingEntity.getUpdatedAt().atOffset(ZoneOffset.UTC));

    return holdingResponse;
  }
}
