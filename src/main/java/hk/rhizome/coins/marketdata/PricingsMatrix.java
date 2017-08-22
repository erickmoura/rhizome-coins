package hk.rhizome.coins.marketdata;

import org.knowm.xchange.currency.CurrencyPair;

import java.util.HashMap;

/**
 * Created by erickmoura on 21/8/2017.
 *
 * Defines and manages the matrix: Exchange X CurrencyPair X Ticker
 */

public class PricingsMatrix {

    private String exchangeId;
    private HashMap<CurrencyPair, ExchangeTicker> pairTicker = new HashMap<CurrencyPair, ExchangeTicker>();

    private static HashMap<String, PricingsMatrix> exchangeInstance = new HashMap<String, PricingsMatrix>();

    private PricingsMatrix(String exchangeId, CurrencyPair pair, ExchangeTicker ticker)
    {
        this.exchangeId = exchangeId;
        addTicker(pair, ticker);
    }

    private void addTicker(CurrencyPair pair, ExchangeTicker ticker){
        pairTicker.put(pair, ticker);
    }

    public static void setTicker(String exchangeId, CurrencyPair pair, ExchangeTicker ticker)
    {
        if(exchangeInstance.get(exchangeId) == null) {
            exchangeInstance.put(exchangeId, new PricingsMatrix(exchangeId, pair, ticker));
        }
        else
        {
            exchangeInstance.get(exchangeId).addTicker(pair, ticker);
        }
    }

    public static ExchangeTicker getTicker(String exchangeId, CurrencyPair pair){

        if(exchangeInstance.get(exchangeId) == null) {
           return null;
        }

        return exchangeInstance.get(exchangeId).pairTicker.get(pair);
    }
}
