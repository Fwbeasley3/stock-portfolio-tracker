package com.portfolio.service.grpc

import com.portfolio.contracts.marketdata.GetStockPriceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification;

@SpringBootTest
public class MarketDataGrpcClientIntegrationSpec extends Specification {

    @Autowired MarketDataGrpcClient marketDataGrpcClient;


    def "Should retrieve stock price from Market Data Service"() {

        when: "Call to marketDataGrpcClient to get stock price and store in GetStockPrice Response"
        GetStockPriceResponse getStockPriceResponse = marketDataGrpcClient.getStockPrice("AAPL")

        then: "Verify the response contains symbol, price and asOf"
        getStockPriceResponse.getSymbol() =="AAPL"
        getStockPriceResponse.getPrice() == "185.50"
        getStockPriceResponse.getAsOf() > 0
    }

}
