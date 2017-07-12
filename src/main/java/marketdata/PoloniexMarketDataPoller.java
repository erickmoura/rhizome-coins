package marketdata;


import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Erick de Moura
 */

public class PoloniexMarketDataPoller extends MarketDataPoller implements Runnable {
  public static final int CORE_POOL_SIZE = 4;

  public boolean running;
  public ScheduledExecutorService ses;


  static {
    init(PoloniexExchange.class.getName());
  }

  public PoloniexMarketDataPoller(CurrencyPair currencyPair) {
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

    Ticker ticker = dataService.getTicker(currencyPair);
    ticker.setExchange("poloniex");
    System.out.println(ticker);
    try {
      kinesisGateway.sendTicker(ticker);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //System.out.println(dataService.getOrderBook(currencyPair));
    //System.out.println(dataService.getOrderBook(currencyPair, 3));
    //System.out.println(dataService.getTrades(currencyPair));
    //long now = new Date().getTime() / 1000;
    //System.out.println(dataService.getTrades(currencyPair, now - 8 * 60 * 60, now));
  }

}
