package hk.rhizome.coins;

import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.bot.CoinMarketCapPoller;
import hk.rhizome.coins.bot.MarketDataPoller;
import hk.rhizome.coins.logger.AppLogger;
import org.knowm.xchange.Exchange;
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
            for(Exchange exchange : ExchangeUtils.getInstance().getBotExchanges()) {

                try {
                    String xchangeName = exchange.getExchangeSpecification().getExchangeName();
                    int exchange_polling = (int) (ExchangeUtils.getInstance().getExchangePollingRate(xchangeName) == null ? POLLING_PERIOD : 60/(0.9*ExchangeUtils.getInstance().getExchangePollingRate(xchangeName)));
                    MarketDataPoller marketDataPoller = new MarketDataPoller(exchange,currencyPair);
                    marketDataPoller.startPolling(i, exchange_polling);
                } catch (Exception e) {
                        AppLogger.getLogger().error("Error in MarketDataManager in startDataMarketThreads : " + e.getLocalizedMessage());
                        e.printStackTrace();
                }
            }
            i++;
        }
    }

    public void startCoinMarketPoller(){
        CoinMarketCapPoller poller = new CoinMarketCapPoller();
        int exchange_polling_rate = 7;
        poller.startPolling(0, exchange_polling_rate);
    }
}
