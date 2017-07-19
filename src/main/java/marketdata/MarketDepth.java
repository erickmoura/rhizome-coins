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

    private BigDecimal ask_05, ask_1, ask_2, ask_5, ask_10;
    private BigDecimal bid_05, bid_1, bid_2, bid_5, bid_10;

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

            if(getAsk_05() == null && limitPrice > min*1.005){
                setAsk_05(sum);
            }

            if(getAsk_1() == null && limitPrice > min*1.01){
                setAsk_1(sum);
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

            if(getBid_05() == null && limitPrice < max*.995){
                setBid_05(sum);
            }

            if(getBid_1() == null && limitPrice < max*.99){
                setBid_1(sum);
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

    public BigDecimal getAsk_05() {
        return ask_05;
    }

    public void setAsk_05(BigDecimal ask_05) {
        this.ask_05 = ask_05;
    }

    public BigDecimal getAsk_1() {
        return ask_1;
    }

    public void setAsk_1(BigDecimal ask_1) {
        this.ask_1 = ask_1;
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

    public BigDecimal getBid_05() {
        return bid_05;
    }

    public void setBid_05(BigDecimal bid_05) {
        this.bid_05 = bid_05;
    }

    public BigDecimal getBid_1() {
        return bid_1;
    }

    public void setBid_1(BigDecimal bid_1) {
        this.bid_1 = bid_1;
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

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {

        return "MarketDepth [exchange: " + exchange + ", currencyPair: " + currencyPair + ", timestamp: " + timestamp +
                ", bid_05=" +  String.valueOf(bid_05) + ", bid_1=" + String.valueOf(bid_1) +
                ", bid_2=" + String.valueOf(bid_2)+ ", bid_5=" + String.valueOf(bid_5) +
                ", bid_10=" + String.valueOf(bid_10) + ", ask_05=" + String.valueOf(ask_05) +
                ", ask_1=" + String.valueOf(ask_1)   + ", ask_2=" + String.valueOf(ask_2) +
                ", ask_5=" + String.valueOf(ask_5)   + ", ask_10=" + String.valueOf(ask_10) + "]";
    }

    public String getExchange() {
        return this.exchange;
    }


    public void setExchange(String exchange) {
        this.exchange = exchange;
    }



}
