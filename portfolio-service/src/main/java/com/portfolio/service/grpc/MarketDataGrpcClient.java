package com.portfolio.service.grpc;

import com.portfolio.contracts.marketdata.GetStockPriceRequest;
import com.portfolio.contracts.marketdata.GetStockPriceResponse;
import com.portfolio.contracts.marketdata.MarketDataServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * gRPC client responsible for communicating with the Market Data Service.
 *
 * Uses a Spring-managed channel and a blocking stub to retrieve stock prices.
 * RPC calls are synchronous and use a deadline to prevent indefinite waiting
 * if the Market Data Service is unavailable or slow to respond.
 */
@Component
@RequiredArgsConstructor
public class MarketDataGrpcClient {

    private final ManagedChannel marketDataChannel;

    public GetStockPriceResponse getStockPrice(String symbol) {

        MarketDataServiceGrpc.MarketDataServiceBlockingStub blockingStub = MarketDataServiceGrpc.newBlockingStub(marketDataChannel);

        GetStockPriceRequest getStockPriceRequest = GetStockPriceRequest.newBuilder().setSymbol(symbol).build();

        blockingStub = blockingStub.withDeadlineAfter(3, TimeUnit.SECONDS);

        return blockingStub.getStockPrice(getStockPriceRequest);
    }
}
