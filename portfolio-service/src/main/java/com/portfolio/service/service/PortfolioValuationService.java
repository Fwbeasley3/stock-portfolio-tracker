package com.portfolio.service.service;

import com.portfolio.contracts.marketdata.GetStockPriceResponse;
import com.portfolio.service.api.model.HoldingValuationResponse;
import com.portfolio.service.api.model.PortfolioValuationResponse;
import com.portfolio.service.grpc.MarketDataGrpcClient;
import com.portfolio.service.persistence.model.HoldingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioValuationService {

    private final HoldingService holdingService;
    private final MarketDataGrpcClient marketDataGrpcClient;

    @Transactional(readOnly = true)
    public PortfolioValuationResponse getValuation(Long portfolioId, Long userId) {

        List<HoldingEntity> holdingEntityList = holdingService.getHoldings(portfolioId, userId);

        List<HoldingValuationResponse> holdingValuationResponseList = new ArrayList<>();

        BigDecimal totalMarketValue = BigDecimal.ZERO;

        for (HoldingEntity holdingEntity: holdingEntityList) {
            GetStockPriceResponse getStockPriceResponse = marketDataGrpcClient.getStockPrice(holdingEntity.getSymbol());

            BigDecimal price = new BigDecimal(getStockPriceResponse.getPrice());
            BigDecimal marketValue = price.multiply(holdingEntity.getQuantity());

            HoldingValuationResponse holdingValuationResponse = new HoldingValuationResponse();
            holdingValuationResponse.setSymbol(holdingEntity.getSymbol());
            holdingValuationResponse.setQuantity(holdingEntity.getQuantity());
            holdingValuationResponse.setCurrentPrice(price);
            holdingValuationResponse.setMarketValue(marketValue);

            holdingValuationResponseList.add(holdingValuationResponse);
            totalMarketValue = totalMarketValue.add(marketValue);
        }

        PortfolioValuationResponse portfolioValuationResponse = new PortfolioValuationResponse();
        portfolioValuationResponse.setPortfolioId(portfolioId);
        portfolioValuationResponse.setHoldings(holdingValuationResponseList);
        portfolioValuationResponse.setTotalMarketValue(totalMarketValue);

        return portfolioValuationResponse;
    }


}
