package levin.analysis.financial.data.crawler.model;

import java.util.Objects;

public class Stock {
    private final String country;
    private final String stockId;
    private final StockPrice price;
    
    private Stock(String country, String stockId, StockPrice price) {
        this.country = country;
        this.stockId = stockId;
        this.price = price;
    }
    
    public static Stock create(String country, String stockId, StockPrice price) {
        return new Stock(country, stockId, price);
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getStockId() {
        return stockId;
    }
    
    public StockPrice getPrice() {
        return price;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(country, stockId);
    }
    
    @Override
    public boolean equals(Object other) {
        
    }
}
