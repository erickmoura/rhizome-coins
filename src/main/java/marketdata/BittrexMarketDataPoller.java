package marketdata;

import org.knowm.xchange.bittrex.v1.BittrexExchange;
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

public class BittrexMarketDataPoller extends MarketDataPoller implements Runnable {
  public static final int CORE_POOL_SIZE = 4;

  public boolean running;
  public ScheduledExecutorService ses;

  public BittrexMarketDataPoller(CurrencyPair currencyPair) {
    super(BittrexExchange.class.getName());
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
      ticker.setExchange("bittrex");
      System.out.println(ticker);

      kinesisGateway.sendTicker(ticker);

      Date timestamp = ticker.getTimestamp();

      OrderBook orderBook = dataService.getOrderBook(currencyPair);

      MarketDepth marketDepth = new MarketDepth(timestamp, orderBook);
      marketDepth.setExchange("bittrex");
      System.out.println(marketDepth);


      //kinesisGateway.sendMarketDepth(marketDepth);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Bittrex: Failed to poll " + currencyPair.toString());
      this.ses.shutdown();

    }



    //System.out.println(dataService.getOrderBook(currencyPair, 3));
    //System.out.println(dataService.getTrades(currencyPair));
    //long now = new Date().getTime() / 1000;
    //System.out.println(dataService.getTrades(currencyPair, now - 8 * 60 * 60, now));
  }

}
