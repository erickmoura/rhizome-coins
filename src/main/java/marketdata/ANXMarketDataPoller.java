package marketdata;


import org.knowm.xchange.anx.v2.ANXExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Erick de Moura
 */

public class ANXMarketDataPoller extends MarketDataPoller implements Runnable {
  public static final int CORE_POOL_SIZE = 4;

  public boolean running;
  public ScheduledExecutorService ses;

  public ANXMarketDataPoller(CurrencyPair currencyPair) {
    super(ANXExchange.class.getName());
    this.currencyPair = currencyPair;
  }

  public void startPolling(long initialDelay, long period) {

    this.ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    this.ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
  }

  public void run() {
    running = true;

    try {
      generic();
    } catch (IOException e) {
      e.printStackTrace();
    }
    running = false;
  }


  private void generic() throws IOException {
    try {

      Ticker ticker = dataService.getTicker(currencyPair);
      ticker.setExchange("anx");
      System.out.println(ticker);
      kinesisGateway.sendTicker(ticker);

      Date timestamp = ticker.getTimestamp();

      OrderBook orderBook = dataService.getOrderBook(currencyPair);

      MarketDepth marketDepth = new MarketDepth(timestamp, orderBook);
      marketDepth.setExchange("anx");
      System.out.println(marketDepth);


      kinesisGateway.sendMarketDepth(marketDepth);

    } catch (Exception e) {
      e.printStackTrace();

      System.out.println("ANX: Failed to poll " + currencyPair.toString());
      this.ses.shutdown();
    }
  }

}
