import marketdata.*;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by erickmoura on 2/7/2017.
 */
public class MarketDataManager {

    public static void main(String[] args) throws Exception {
        MarketDataManager m = new MarketDataManager();
        m.startDataMarketThreads();
    }

    public void startDataMarketThreads() {

        int i = 0;
        for(CurrencyPair currencyPair : CurrencySetService.getCurrencySet())
        {

            try {
                ANXMarketDataPoller anxMarketDataPoller = new ANXMarketDataPoller(currencyPair);
                anxMarketDataPoller.startPolling(i,12);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                GatecoinMarketDataPoller gatecoinMarketDataPoller = new GatecoinMarketDataPoller(currencyPair);
                gatecoinMarketDataPoller.startPolling(i,12);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                BittrexMarketDataPoller bittrexMarketDataPoller = new BittrexMarketDataPoller(currencyPair);
                bittrexMarketDataPoller.startPolling(i,12);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                PoloniexMarketDataPoller poloniexMarketDataPoller = new PoloniexMarketDataPoller(currencyPair);
                poloniexMarketDataPoller.startPolling(i,12);
            } catch (Exception e) {
                e.printStackTrace();
            }

            i++;
        }
    }
}
