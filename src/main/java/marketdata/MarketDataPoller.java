package marketdata;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.KinesisGateway;
import org.knowm.xchange.anx.v2.ANXExchange;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.CertHelper;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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
    protected static HashMap<String, Exchange> exchanges = new HashMap<String, Exchange>();


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

            Ticker ticker = dataServices.get(exchangeId).getTicker(currencyPair);
            ticker.setExchange(exchangeId);
            System.out.println(ticker);
            kinesisGateway.sendTicker(ticker);

            Date timestamp = ticker.getTimestamp();

            OrderBook orderBook = dataServices.get(exchangeId).getOrderBook(currencyPair);

            MarketDepth marketDepth = new MarketDepth(timestamp, orderBook);
            marketDepth.setExchange(exchangeId);
            System.out.println(marketDepth);


            kinesisGateway.sendMarketDepth(marketDepth);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(exchangeId + ": Failed to poll " + currencyPair.toString());
            //this.ses.shutdown();
            throw(e);
        }
    }


    public MarketDataPoller(String exchangeId, String exchangeClassName, CurrencyPair currencyPair){

        this.exchangeId = exchangeId;
        this.currencyPair = currencyPair;

        if(this.exchanges.get(exchangeId) == null)
        {
            this.exchanges.put(exchangeId, ExchangeFactory.INSTANCE.createExchange(exchangeClassName));
        }

        if(this.dataServices.get(exchangeId) == null) {
            this.dataServices.put(exchangeId, exchanges.get(exchangeId).getMarketDataService());
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
