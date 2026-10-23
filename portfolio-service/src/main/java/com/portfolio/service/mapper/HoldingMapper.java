package com.portfolio.service.mapper;

import com.portfolio.service.api.model.HoldingResponse;
import com.portfolio.service.persistence.model.HoldingEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HoldingMapper {

    public static HoldingResponse mapToResponse(HoldingEntity holdingEntity) {

        HoldingResponse holdingResponse = new HoldingResponse();
        holdingResponse.setSymbol(holdingEntity.getSymbol());
        holdingResponse.setQuantity(holdingEntity.getQuantity());
        holdingResponse.setAverageCost(holdingEntity.getAverageCost());

        return holdingResponse;
    }
}
