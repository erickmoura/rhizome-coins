package hk.rhizome.coins;

import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.marketdata.MarketDataPoller;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * Created by erickmoura on 2/7/2017.
 */
public class MarketDataManager {


    public static final int POLLING_PERIOD = 12; //seconds

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
