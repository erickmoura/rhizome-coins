import marketdata.BittrexMarketDataPoller;
import marketdata.CurrencySetService;
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
                BittrexMarketDataPoller bittrexMarketDataPoller = new BittrexMarketDataPoller(currencyPair);
                bittrexMarketDataPoller.startPolling(i,12);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
