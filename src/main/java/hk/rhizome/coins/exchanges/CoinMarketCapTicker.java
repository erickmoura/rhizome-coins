package hk.rhizome.coins.exchanges;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinMarketCapTicker {

  private static String NAME_EXCHANGE = "CoinMarketCap";

  private String exchange;
  private String id;
  private String name;
  private String symbol;
  private int rank;
  private BigDecimal priceUSD;
  private BigDecimal priceBTC;
  private BigDecimal t4hvolumeUSD;
  private BigDecimal marketCapUSD;
  private BigDecimal availableSupply;
  private BigDecimal totalSupply;
  private BigDecimal percentChange1h;
  private BigDecimal percentChange24h;
  private BigDecimal percentChange7d;
  private Date lastUpdated; 

  @JsonCreator
  public CoinMarketCapTicker(@JsonProperty("id") String id, @JsonProperty("name") String name, 
                            @JsonProperty("symbol") String symbol, @JsonProperty("rank") int rank,
                            @JsonProperty("price_usd") BigDecimal priceUSD, @JsonProperty("price_btc") BigDecimal priceBTC,
                            @JsonProperty("24h_volume_usd") BigDecimal t4hvolumeUSD, @JsonProperty("market_cap_usd") BigDecimal marketCapUSD, 
                            @JsonProperty("available_supply") BigDecimal availableSupply, @JsonProperty("total_supply") BigDecimal totalSupply, 
                            @JsonProperty("percent_change_1h") BigDecimal percentChange1h, @JsonProperty("percent_change_24h") BigDecimal percentChange24h, 
                            @JsonProperty("percent_change_7d") BigDecimal percentChange7d, @JsonProperty("last_updated") Long lastUpdated) {

    this.exchange = NAME_EXCHANGE;
    this.id = id;
    this.name = name;
    this.symbol = symbol;
    this.rank = rank;
    this.priceUSD = priceUSD;
    this.priceBTC = priceBTC;
    this.t4hvolumeUSD = t4hvolumeUSD;
    this.marketCapUSD = marketCapUSD;
    this.availableSupply = availableSupply;
    this.totalSupply = totalSupply;
    this.percentChange1h = percentChange1h;
    this.percentChange24h = percentChange24h;
    this.percentChange7d = percentChange7d;
    this.lastUpdated = new Date(lastUpdated*1000);
  }

  public CoinMarketCapTicker(String exchangeName, String id, String name,
                              String symbol, int rank,
                              BigDecimal priceUSD, BigDecimal priceBTC,
                              BigDecimal t4hvolumeUSD, BigDecimal marketCapUSD,
                              BigDecimal availableSupply,
                              BigDecimal totalSupply,
                              BigDecimal percentChange1h,
                              BigDecimal percentChange24h,
                              BigDecimal percentChange7d, Long lastUpdated) {

    this.exchange = exchangeName;
    this.id = id;
    this.name = name;
    this.symbol = symbol;
    this.rank = rank;
    this.priceUSD = priceUSD;
    this.priceBTC = priceBTC;
    this.t4hvolumeUSD = t4hvolumeUSD;
    this.marketCapUSD = marketCapUSD;
    this.availableSupply = availableSupply;
    this.totalSupply = totalSupply;
    this.percentChange1h = percentChange1h;
    this.percentChange24h = percentChange24h;
    this.percentChange7d = percentChange7d;
    this.lastUpdated = new Date(lastUpdated * 1000);
  }

  public CoinMarketCapTicker(Map<String, Object> data){
    this.id = (String)data.get("id");
    this.name = (String)data.get("name");
    this.symbol = (String)data.get("symbol");
    this.rank = (Integer)data.get("rank");
    this.priceUSD = (BigDecimal)data.get("price_usd");
    this.priceBTC = (BigDecimal)data.get("price_btc");
    this.t4hvolumeUSD = (BigDecimal)data.get("24h_volume_usd");
    this.marketCapUSD = (BigDecimal)data.get("market_cap_usd");
    this.availableSupply = (BigDecimal)data.get("available_supply");
    this.totalSupply = (BigDecimal)data.get("total_supply");
    this.percentChange1h = (BigDecimal)data.get("percent_change_1h");
    this.percentChange24h = (BigDecimal)data.get("percent_change_24h");
    this.percentChange7d = (BigDecimal)data.get("percent_change_7d");
    this.lastUpdated = (Date)data.get("last_updated");
  }

  public String getExchange(){
    return this.exchange;
  }
  public void setExchange(String exchange){
    this.exchange = exchange;
  }

  public String getId(){
    return this.id;
  }

  public void setId(String id){
    this.id = id;
  }

  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }

  public String getSymbol(){
    return this.symbol;
  }

  public void setSymbol(String symbol){
    this.symbol = symbol;
  }

  public int getRank(){
    return this.rank;
  }

  public void setRank(int rank){
    this.rank = rank;
  }

  public BigDecimal getPriceUSD(){
    return this.priceUSD;
  }

  public void setPriceUSD(BigDecimal priceUSD){
    this.priceUSD = priceUSD;
  }

  public BigDecimal getPriceBTC(){
    return this.priceBTC;
  }

  public void setPriceBTC(BigDecimal priceBTC){
    this.priceBTC = priceBTC;
  }

  public BigDecimal getT4hVolumeUSD(){
    return this.t4hvolumeUSD;
  }

  public void setT4hVolumeUSD(BigDecimal volume){
    this.t4hvolumeUSD = volume;
  }

  public BigDecimal getMarketCapUSD(){
    return this.marketCapUSD;
  }

  public void setMarketCapUSD(BigDecimal marketCapUSD){
    this.marketCapUSD = marketCapUSD;
  }
  
  public BigDecimal getAvailableSupply(){
    return this.availableSupply;
  }

  public void setAvailableSupply(BigDecimal availableSupply){
    this.availableSupply = availableSupply;
  }

  public BigDecimal getTotalSupply(){
    return this.totalSupply;
  }

  public void setTotalSupply(BigDecimal totalSupply){
    this.totalSupply = totalSupply;
  }

  public BigDecimal getPercentChange1h(){
    return this.percentChange1h;
  }

  public void setPercentChange1h(BigDecimal percentChange1h){
    this.percentChange1h = percentChange1h;
  }

  public BigDecimal getPercentChange24h(){
    return this.percentChange24h;
  }

  public void setPercentChange24h(BigDecimal percentChange24h){
    this.percentChange24h = percentChange24h;
  }

  public BigDecimal getPercentChange7d(){
    return this.percentChange7d;
  }

  public void setPercentChange7d(BigDecimal percentChange7d){
    this.percentChange7d = percentChange7d;
  }
  
  public Date getLastUpdated(){
    return this.lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated){
    this.lastUpdated = lastUpdated;
  }

  @Override
  public String toString() {

    return "CoinMarketCapTicker { exchange : " + exchange + ", id : " + getId() + ", name : " + getName() + ", symbol : " + getSymbol() 
            + ", rank" + getRank() + ", price usd : " + getPriceUSD() + ", price btc" + getPriceBTC() + ", 24h volume usd : " + getT4hVolumeUSD() 
            + ", market_cap_usd : " + getMarketCapUSD() + ", available supply : " + getAvailableSupply() + ", total supply : " + getTotalSupply() 
            + ", percentage change 1h : " + getPercentChange1h() + ", percentage change 24h : " + getPercentChange24h() 
            + ", percentage change 7d : " + getPercentChange7d() + ", last updated : " + getLastUpdated() + "}" ;
  }

}
