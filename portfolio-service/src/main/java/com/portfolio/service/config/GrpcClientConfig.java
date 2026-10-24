package com.portfolio.service.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdown")
    ManagedChannel marketDataChannel(
            @Value("${market-data.grpc.host:localhost}") String host,
            @Value("${market-data.grpc.port:9090}") int port) {

        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }
}