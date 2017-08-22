package hk.rhizome.coins.marketdata;

import org.knowm.xchange.currency.CurrencyPair;

import java.util.HashMap;

/**
 * Created by erickmoura on 21/8/2017.
 *
 * Defines and manages the matrix: Exchange X CurrencyPair X TradingFeePair
 */

public class FeesMatrix {

    private String exchangeId;
    private TradingFeePair tradingFeePair;

    private static HashMap<String, FeesMatrix> exchangeInstance = new HashMap<String, FeesMatrix>();

    private FeesMatrix(String exchangeId, TradingFeePair fees)
    {
        this.exchangeId = exchangeId;
        this.tradingFeePair = fees;
    }

    public static void setFeesMatrix(String exchangeId, TradingFeePair fees)
    {
        if(exchangeInstance.get(exchangeId) == null) {
            exchangeInstance.put(exchangeId, new FeesMatrix(exchangeId, fees));
        }
        else
        {
            exchangeInstance.get(exchangeId).tradingFeePair = fees;
        }
    }

    public static TradingFeePair getFees(String exchangeId){

        if(exchangeInstance.get(exchangeId) == null) {
           return null;
        }

        return exchangeInstance.get(exchangeId).tradingFeePair;
    }
}
