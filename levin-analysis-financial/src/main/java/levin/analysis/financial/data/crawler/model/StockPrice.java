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
    
    private StockPrice() { }
    
    public static StockPrice create(DateTime dateTime, Double openPrice, Double ceilingPrice,
            Double closePrice, Double floorPrice, Long turnover, Double priceFluctuation,
            Double priceChangePercentage, Double priceAvgIn5Days, Double priceAvgIn10Days,
            Double priceAvgIn20Days, Long turnoverAvgIn5Days, Long turnoverAvgIn10Days,
            Long turnoverAvgIn20Days, Double turnoverRate) {
        StockPrice stock = new StockPrice();
        stock.dateTime = dateTime;
        stock.openPrice = openPrice;
        stock.ceilingPrice = ceilingPrice;
        stock.closePrice = closePrice;
        stock.floorPrice = floorPrice;
        
        return stock;
    }
}
