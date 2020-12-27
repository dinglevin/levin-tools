package com.dinglevin.tools.analysis.financial.data.crawler.model;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(country, stock.country) &&
                Objects.equals(stockId, stock.stockId) &&
                Objects.equals(price, stock.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, stockId, price);
    }
}
