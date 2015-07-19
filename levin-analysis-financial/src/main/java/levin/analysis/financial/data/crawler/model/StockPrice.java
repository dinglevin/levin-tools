package levin.analysis.financial.data.crawler.model;

import org.joda.time.DateTime;


/**
 * 日期 ，开盘价， 最高价， 收盘价， 最低价， 成交量， 价格变动 ，涨跌幅，5日均价，10日均价，20日均价，5日均量，10日均量，20日均量，换手率
 * 
 * @author Levin
 */
public class StockPrice {
    private final DateTime dateTime;
    private final Double openPrice;
    private final Double ceilingPrice;
    private final Double closePrice;
    private final Double floorPrice;
    private final Long turnover;
    private final Double priceFluctuation;
    private final Double priceChangePercentage;
    private final Double priceAvgIn5Days;
    private final Double priceAvgIn10Days;
    private final Double priceAvgIn20Days;
    private final Long turnoverAvgIn5Days;
    private final Long turnoverAvgIn10Days;
    private final Long turnoverAvgIn20Days;
    private final Double turnoverRate;
    
    private StockPrice(DateTime dateTime, Double openPrice, Double ceilingPrice,
            Double closePrice, Double floorPrice, Long turnover, Double priceFluctuation,
            Double priceChangePercentage, Double priceAvgIn5Days, Double priceAvgIn10Days,
            Double priceAvgIn20Days, Long turnoverAvgIn5Days, Long turnoverAvgIn10Days,
            Long turnoverAvgIn20Days, Double turnoverRate) {
        this.dateTime = dateTime;
        this.openPrice = openPrice;
        this.ceilingPrice = ceilingPrice;
        this.closePrice = closePrice;
        this.floorPrice = floorPrice;
        this.turnover = turnover;
        this.priceFluctuation = priceFluctuation;
        this.priceChangePercentage = priceChangePercentage;
        this.priceAvgIn5Days = priceAvgIn5Days;
        this.priceAvgIn10Days = priceAvgIn10Days;
        this.priceAvgIn20Days = priceAvgIn20Days;
        this.turnoverAvgIn5Days = turnoverAvgIn5Days;
        this.turnoverAvgIn10Days = turnoverAvgIn10Days;
        this.turnoverAvgIn20Days = turnoverAvgIn20Days;
        this.turnoverRate = turnoverRate;
    }
    
    public static StockPrice create(DateTime dateTime, Double openPrice, Double ceilingPrice,
            Double closePrice, Double floorPrice, Long turnover, Double priceFluctuation,
            Double priceChangePercentage, Double priceAvgIn5Days, Double priceAvgIn10Days,
            Double priceAvgIn20Days, Long turnoverAvgIn5Days, Long turnoverAvgIn10Days,
            Long turnoverAvgIn20Days, Double turnoverRate) {
        return new StockPrice(dateTime, openPrice, ceilingPrice, closePrice, floorPrice, turnover, 
                priceFluctuation, priceChangePercentage, priceAvgIn5Days, priceAvgIn10Days,
                priceAvgIn20Days, turnoverAvgIn5Days, turnoverAvgIn10Days, turnoverAvgIn20Days, turnoverRate);
    }
    
    public DateTime getDateTime() {
        return dateTime;
    }
    
    public Double getOpenPrice() {
        return openPrice;
    }
    
    public Double getCeilingPrice() {
        return ceilingPrice;
    }
    
    public Double getClosePrice() {
        return closePrice;
    }
    
    public Double getFloorPrice() {
        return floorPrice;
    }
    
    public Long getTurnover() {
        return turnover;
    }
    
    public Double getPriceFluctuation() {
        return priceFluctuation;
    }
    
    public Double getPriceChangePercentage() {
        return priceChangePercentage;
    }
    
    public Double getPriceAvgIn5Days() {
        return priceAvgIn5Days;
    }
    
    public Double getPriceAvgIn10Days() {
        return priceAvgIn10Days;
    }
    
    public Double getPriceAvgIn20Days() {
        return priceAvgIn20Days;
    }
    
    public Long getTurnoverAvgIn5Days() {
        return turnoverAvgIn5Days;
    }
    
    public Long getTurnoverAvgIn10Days() {
        return turnoverAvgIn10Days;
    }
    
    public Long getTurnoverAvgIn20Days() {
        return turnoverAvgIn20Days;
    }
    
    public Double getTurnoverRate() {
        return turnoverRate;
    }

    @Override
    public String toString() {
        return "StockPrice [openPrice=" + openPrice + ", ceilingPrice="
                + ceilingPrice + ", closePrice=" + closePrice + ", floorPrice="
                + floorPrice + ", turnover=" + turnover + ", priceFluctuation="
                + priceFluctuation + ", priceChangePercentage="
                + priceChangePercentage + ", priceAvgIn5Days="
                + priceAvgIn5Days + ", priceAvgIn10Days=" + priceAvgIn10Days
                + ", priceAvgIn20Days=" + priceAvgIn20Days
                + ", turnoverAvgIn5Days=" + turnoverAvgIn5Days
                + ", turnoverAvgIn10Days=" + turnoverAvgIn10Days
                + ", turnoverAvgIn20Days=" + turnoverAvgIn20Days
                + ", turnoverRate=" + turnoverRate + "]";
    }
}
