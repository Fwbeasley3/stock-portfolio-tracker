package com.portfolio.service.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean (destroyMethod = "shutdown")
    ManagedChannel marketDataChannel() {

        return ManagedChannelBuilder.forAddress("localHost", 9090).usePlaintext().build();
    }
}
