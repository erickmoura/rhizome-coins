package marketdata;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.ExchangeBound;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by erickmoura on 11/7/2017.
 */
public final class MarketDepth implements ExchangeBound {

    private String exchange;
    private CurrencyPair currencyPair;
    private Date timestamp;

    private BigDecimal ask_2, ask_5, ask_10, ask_15;
    private BigDecimal bid_2, bid_5, bid_10, bid_15;

    public MarketDepth(Date timestamp, OrderBook orderBook) {

        this.timestamp = timestamp;
        
        BigDecimal sum = BigDecimal.valueOf(0);
        double min = 0, max = 0;
        
        for(LimitOrder limitOrder : orderBook.getAsks()){
            double limitPrice = limitOrder.getLimitPrice().doubleValue();
            BigDecimal tradableAmount = limitOrder.getTradableAmount();
            if(sum.doubleValue() == 0)
            {
                min = limitPrice;
                sum = tradableAmount;
                this.currencyPair = limitOrder.getCurrencyPair();
            }

            if(getAsk_2() == null && limitPrice > min*1.02){
                setAsk_2(sum);
            }

            if(getAsk_5() == null && limitPrice > min*1.05){
                setAsk_5(sum);
            }

            if(getAsk_10() == null && limitPrice > min*1.1){
                setAsk_10(sum);
            }

            if(getAsk_15() == null && limitPrice > min*1.15){
                setAsk_15(sum);
            }

            sum = sum.add(tradableAmount);
        }

        sum = BigDecimal.valueOf(0);
        for(LimitOrder limitOrder : orderBook.getBids()){
            double limitPrice = limitOrder.getLimitPrice().doubleValue();
            BigDecimal tradableAmount = limitOrder.getTradableAmount();
            if(sum.doubleValue() == 0)
            {
                max = limitPrice;
                sum = tradableAmount;
            }

            if(getBid_2() == null && limitPrice < max*.98){
                setBid_2(sum);
            }

            if(getBid_5() == null && limitPrice < max*.95){
                setBid_5(sum);
            }

            if(getBid_10() == null && limitPrice < max*.9){
                setBid_10(sum);
            }

            if(getBid_15() == null && limitPrice < max*.85){
                setBid_15(sum);
            }

            sum = sum.add(tradableAmount);
        }

    }

    public CurrencyPair getCurrencyPair()
    {
        return currencyPair;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public BigDecimal getAsk_2() {
        return ask_2;
    }

    public void setAsk_2(BigDecimal ask_2) {
        this.ask_2 = ask_2;
    }

    public BigDecimal getAsk_5() {
        return ask_5;
    }

    public void setAsk_5(BigDecimal ask_5) {
        this.ask_5 = ask_5;
    }

    public BigDecimal getAsk_10() {
        return ask_10;
    }

    public void setAsk_10(BigDecimal ask_10) {
        this.ask_10 = ask_10;
    }

    public BigDecimal getAsk_15() {
        return ask_15;
    }

    public void setAsk_15(BigDecimal ask_15) {
        this.ask_15 = ask_15;
    }

    public BigDecimal getBid_2() {
        return bid_2;
    }

    public void setBid_2(BigDecimal bid_2) {
        this.bid_2 = bid_2;
    }

    public BigDecimal getBid_5() {
        return bid_5;
    }

    public void setBid_5(BigDecimal bid_5) {
        this.bid_5 = bid_5;
    }

    public BigDecimal getBid_10() {
        return bid_10;
    }

    public void setBid_10(BigDecimal bid_10) {
        this.bid_10 = bid_10;
    }

    public BigDecimal getBid_15() {
        return bid_15;
    }

    public void setBid_15(BigDecimal bid_15) {
        this.bid_15 = bid_15;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {

        return "MarketDepth [exchange: " + exchange + ", currencyPair: " + currencyPair + ", timestamp: " + timestamp + ", bid_2=" + bid_2.toString() + ", bid_5=" + bid_5.toString() +
                ", bid_10=" + bid_10.toString()+ ", bid_15=" + bid_15.toString() + ", ask_2=" + ask_2.toString() +
                ", ask_5=" + ask_5.toString()  + ", ask_10=" + ask_10.toString() + ", ask_15=" + ask_15.toString()+ "]";
    }

    public String getExchange() {
        return this.exchange;
    }


    public void setExchange(String exchange) {
        this.exchange = exchange;
    }



}
