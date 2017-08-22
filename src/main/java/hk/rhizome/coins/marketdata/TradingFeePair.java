package hk.rhizome.coins.marketdata;

import java.math.BigDecimal;

/**
 * Created by erickmoura on 21/8/2017.
 */
public class TradingFeePair {

    private BigDecimal maker;
    private BigDecimal taker;

    public TradingFeePair(BigDecimal maker, BigDecimal taker){
        this.maker = maker;
        this.taker = taker;
    }

    public BigDecimal getMaker() {
        return maker;
    }

    public BigDecimal getTaker() {
        return taker;
    }
}
