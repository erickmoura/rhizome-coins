package marketdata;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class CurrencySetService {

    private static Set<CurrencyPair> currencyPairs = new HashSet<CurrencyPair>();

    static{
        currencyPairs.add(CurrencyPair.BTC_USD);
        currencyPairs.add(CurrencyPair.LTC_BTC);
        currencyPairs.add(CurrencyPair.ETH_BTC);
        currencyPairs.add(CurrencyPair.DGC_BTC);
        currencyPairs.add(CurrencyPair.XRP_BTC);
        currencyPairs.add(CurrencyPair.STR_BTC);
    }

    public static Set<CurrencyPair> getCurrencySet()
    {
        return currencyPairs;
    }
}
