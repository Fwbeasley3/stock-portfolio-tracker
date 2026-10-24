package portfolio.marketdata.config;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import portfolio.marketdata.grpc.MarketDataGrpcService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class GrpcServerLifecycle {

    private final MarketDataGrpcService marketDataGrpcService;
    private Server server;

    /**
     * Starts the gRPC server after Spring initializes this bean
     * and injects its dependencies.
     */
    @PostConstruct
    public void start() throws IOException {

        int port = 9090;

        server = NettyServerBuilder.forPort(port).addService(marketDataGrpcService).build().start();

    }

    /**
     * Initiates graceful gRPC server shutdown when Spring destroys this bean.
     * Waits up to 5 seconds for active RPCs to finish before forcing shutdown.
     */
    @PreDestroy
    public void stop() throws InterruptedException {

        if (server == null) {
            return;
        }

        // Initiate graceful shutdown
        server.shutdown();

        try {
            if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
                server.shutdownNow();
            }
        } catch (InterruptedException ex) {
            server.shutdownNow();
            // Set interrupted flag back to true since Interrupted Exception clears the flag
            Thread.currentThread().interrupt();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if(server != null) {
            server.awaitTermination();
        }
    }

    @Bean
    public ApplicationRunner applicationRunner (GrpcServerLifecycle grpcServerLifecycle) throws InterruptedException {
        return args -> grpcServerLifecycle.blockUntilShutdown();
    }
}
