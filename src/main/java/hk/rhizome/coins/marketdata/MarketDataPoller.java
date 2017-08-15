package hk.rhizome.coins.marketdata;

import hk.rhizome.coins.KinesisGateway;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.CertHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class MarketDataPoller  implements Runnable  {

    protected static final int CORE_POOL_SIZE = 50;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected static KinesisGateway kinesisGateway = new KinesisGateway();;
    protected static HashMap<String, MarketDataService> dataServices = new HashMap<String, MarketDataService>();


    protected String exchangeId;
    protected CurrencyPair currencyPair;

    public boolean running;

    public void startPolling(long initialDelay, long period) {

        try {
            generic();
            ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;

        try {
            generic();
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = false;
    }


    private void generic() throws Exception {
        try {

            // Collect Ticker data
            ExchangeTicker ticker = new ExchangeTicker(exchangeId, dataServices.get(exchangeId).getTicker(currencyPair));
            System.out.println(ticker);
            kinesisGateway.sendTicker(ticker);

            // Collect Order Book data
            Date timestamp = ticker.getTimestamp();
            OrderBook orderBook = dataServices.get(exchangeId).getOrderBook(currencyPair);

            MarketDepth marketDepth = new MarketDepth(timestamp, orderBook);
            marketDepth.setExchange(exchangeId);
            System.out.println(marketDepth);
            kinesisGateway.sendMarketDepth(marketDepth);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(exchangeId + ": Failed to poll " + currencyPair.toString());
            throw(e);
        }
    }


    public MarketDataPoller(Exchange exchange, CurrencyPair currencyPair){

        this.exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
        this.currencyPair = currencyPair;

        if(this.dataServices.get(exchangeId) == null) {
            this.dataServices.put(exchangeId, exchange.getMarketDataService());
        }

        try {
            kinesisGateway.validateStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CertHelper.trustAllCerts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
