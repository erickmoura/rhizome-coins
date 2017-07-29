import marketdata.CurrencySetService;
import marketdata.MarketDataPoller;
import org.knowm.xchange.anx.v2.ANXExchange;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gatecoin.GatecoinExchange;
import org.knowm.xchange.poloniex.ExchangeUtils;
import org.knowm.xchange.poloniex.PoloniexExchange;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by erickmoura on 2/7/2017.
 */
public class MarketDataManager {


    public static final int POLLING_PERIOD = 12; //seconds

    /*
    static{
        Hashtable<String, String> tmp =
                new Hashtable<String, String>();

        tmp.put("anx", ANXExchange.class.getName());
        //tmp.put("gatecoin", GatecoinExchange.class.getName());
        tmp.put("bittrex", BittrexExchange.class.getName());
        tmp.put("poloniex", PoloniexExchange.class.getName());
        EXCHANGES = Collections.unmodifiableMap(tmp);
    }
*/


    public static void main(String[] args) throws Exception {
        MarketDataManager m = new MarketDataManager();
        m.startDataMarketThreads();
    }


    public void startDataMarketThreads() {


        int i = 0;
        for(CurrencyPair currencyPair : CurrencySetService.getCurrencySet())
        {
            for(String key : ExchangeUtils.getInstance().getExchangeClassNames()) {

                try {
                    MarketDataPoller marketDataPoller = new MarketDataPoller(ExchangeUtils.getInstance().getExchange(key),currencyPair);
                    marketDataPoller.startPolling(i, POLLING_PERIOD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
    }
}
