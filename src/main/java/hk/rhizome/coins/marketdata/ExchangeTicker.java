package hk.rhizome.coins.marketdata;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by erickmoura on 12/8/2017.
 */
public class ExchangeTicker {

    private final String exchange;
    private final CurrencyPair currencyPair;
    private final BigDecimal last;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal vwap;
    private final BigDecimal volume;
    /**
     * the timestamp of the ticker according to the exchange's server, null if not provided
     */
    private final Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public ExchangeTicker(String exchange, Ticker ticker) {
        this.exchange = exchange;
        this.currencyPair = ticker.getCurrencyPair();
        this.last = ticker.getLast();
        this.ask = ticker.getAsk();
        this.bid = ticker.getBid();
        this.high = ticker.getHigh();
        this.low = ticker.getLow();
        this.vwap = ticker.getVwap();
        this.volume = ticker.getVolume();
        this.timestamp = (ticker.getTimestamp() == null) ? new Date() : ticker.getTimestamp();

    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public BigDecimal getLast() {
        return last;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getVwap() {
        return vwap;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public String getExchange() {

        return exchange;
    }

    @Override
    public String toString() {
        return "Ticker [exchange=" + exchange + ", exchcurrencyPair=" + currencyPair + ", last=" + last + ", bid=" + bid + ", ask=" + ask + ", high=" + high + ", low=" + low + ",avg="
                + vwap + ", volume=" + volume + ", timestamp=" + DateUtils.toMillisNullSafe(timestamp) + "]";
    }

}
