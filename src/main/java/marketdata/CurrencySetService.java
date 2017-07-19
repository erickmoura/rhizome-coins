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
        currencyPairs.add(new CurrencyPair(Currency.BTC, Currency.USDT));
        currencyPairs.add(new CurrencyPair(Currency.BTC, Currency.USD));
        currencyPairs.add(new CurrencyPair(Currency.LTC, Currency.BTC));
        currencyPairs.add(new CurrencyPair(Currency.ETH, Currency.BTC));
        currencyPairs.add(new CurrencyPair(Currency.DGB, Currency.BTC));
        currencyPairs.add(new CurrencyPair(Currency.XMR, Currency.BTC));
        currencyPairs.add(new CurrencyPair(Currency.XRP, Currency.BTC));
    }

    public static Set<CurrencyPair> getCurrencySet()
    {
        return currencyPairs;
    }
}
