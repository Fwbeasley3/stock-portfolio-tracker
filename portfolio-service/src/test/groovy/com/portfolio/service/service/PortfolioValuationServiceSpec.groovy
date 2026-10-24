package com.portfolio.service.service

import com.portfolio.contracts.marketdata.GetStockPriceResponse
import com.portfolio.service.api.model.PortfolioValuationResponse
import com.portfolio.service.grpc.MarketDataGrpcClient
import com.portfolio.service.persistence.model.HoldingEntity
import com.portfolio.service.persistence.model.PortfolioEntity
import com.portfolio.service.persistence.model.UserEntity
import spock.lang.Specification

class PortfolioValuationServiceSpec extends Specification {

    HoldingService holdingService = Mock(HoldingService)
    MarketDataGrpcClient marketDataGrpcClient = Mock(MarketDataGrpcClient)
    PortfolioValuationService portfolioValuationService = new PortfolioValuationService(holdingService,marketDataGrpcClient)

    def "Should calculate portfolio valuation for multiple holdings"() {
        given: "A portfolio and two holdings"

        Long userId = 1L
        Long portfolioId = 1L

        UserEntity userEntity = new UserEntity("test-id-123")
        PortfolioEntity portfolioEntity = new PortfolioEntity(userEntity, "Retirement")

        HoldingEntity holdingEntity = new HoldingEntity(portfolioEntity, "AAPL", new BigDecimal("10"), new BigDecimal("150.00"))
        HoldingEntity holdingEntity1 = new HoldingEntity(portfolioEntity, "MSFT", new BigDecimal("5"), new BigDecimal("300.00"))

        List<HoldingEntity> holdingEntityList = [holdingEntity, holdingEntity1]

        GetStockPriceResponse aaplResponse = GetStockPriceResponse.newBuilder()
                .setSymbol("AAPL")
                .setPrice("185.50")
                .setAsOf(System.currentTimeMillis())
                .build()

        GetStockPriceResponse msftResponse = GetStockPriceResponse.newBuilder()
                .setSymbol("MSFT")
                .setPrice("400.00")
                .setAsOf(System.currentTimeMillis())
                .build()

        when: "The valuation is retrieved"
        PortfolioValuationResponse portfolioValuationResponse = portfolioValuationService.getValuation(portfolioId, userId)

        then: "Verify holdings are retrieved"
        1 * holdingService.getHoldings(portfolioId, userId) >> holdingEntityList
        1 * marketDataGrpcClient.getStockPrice("AAPL") >> aaplResponse
        1 * marketDataGrpcClient.getStockPrice("MSFT") >> msftResponse

        and: "Verify portfolioValuationResponse values are set correctly"
        portfolioValuationResponse.getPortfolioId() == portfolioId
        portfolioValuationResponse.getHoldings().size() == 2
        portfolioValuationResponse.getHoldings().get(0).getSymbol() == "AAPL"
        portfolioValuationResponse.getHoldings().get(0).getCurrentPrice() == new BigDecimal("185.50")
        portfolioValuationResponse.getHoldings().get(0).getMarketValue() == new BigDecimal("1855.00")
        portfolioValuationResponse.getHoldings().get(1).getSymbol() == "MSFT"
        portfolioValuationResponse.getHoldings().get(1).getCurrentPrice() == new BigDecimal("400.00")
        portfolioValuationResponse.getHoldings().get(1).getMarketValue() == new BigDecimal("2000.00")
        portfolioValuationResponse.getTotalMarketValue() == new BigDecimal("3855.00")
    }
}
