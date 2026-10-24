package portfolio.marketdata.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import portfolio.marketdata.model.StockPrice;

import java.math.BigDecimal;
import java.util.Locale;

@Service
public class MarketPriceService {

    public StockPrice getPrice(String symbol) {

        String validSymbol = validateAndNormalizeSymbol(symbol);

        BigDecimal hardCodedPrice = new BigDecimal("185.50");

        return new StockPrice(validSymbol, hardCodedPrice, System.currentTimeMillis());
    }

    private String validateAndNormalizeSymbol (String symbol) {

        if (StringUtils.isBlank(symbol)) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }

        return symbol.trim().toUpperCase(Locale.ROOT);
    }
}
