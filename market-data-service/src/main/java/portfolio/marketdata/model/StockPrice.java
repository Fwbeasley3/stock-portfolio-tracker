package portfolio.marketdata.model;

import java.math.BigDecimal;

public record StockPrice(
        String symbol,
        BigDecimal price,
        long asOf
) {
}
