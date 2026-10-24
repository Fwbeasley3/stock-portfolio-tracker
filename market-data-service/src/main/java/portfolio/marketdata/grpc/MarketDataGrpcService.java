package portfolio.marketdata.grpc;

import com.portfolio.contracts.marketdata.GetStockPriceRequest;
import com.portfolio.contracts.marketdata.GetStockPriceResponse;
import com.portfolio.contracts.marketdata.MarketDataServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import portfolio.marketdata.model.StockPrice;
import portfolio.marketdata.service.MarketPriceService;


@Service
@RequiredArgsConstructor
public class MarketDataGrpcService extends MarketDataServiceGrpc.MarketDataServiceImplBase {

    private final MarketPriceService marketPriceService;

    @Override
    public void getStockPrice(GetStockPriceRequest request, StreamObserver<GetStockPriceResponse> responseObserver) {

        String symbol = request.getSymbol();

        try {
            StockPrice stockPrice = marketPriceService.getPrice(symbol);

            GetStockPriceResponse getStockPriceResponse = GetStockPriceResponse.newBuilder().setSymbol(stockPrice.symbol()).setPrice(stockPrice.price().toPlainString()).setAsOf(stockPrice.asOf()).build();

            // Sends response
            responseObserver.onNext(getStockPriceResponse);
            // signal for successful completion
            responseObserver.onCompleted();


        } catch (IllegalArgumentException ex) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException());
        }
    }
}
