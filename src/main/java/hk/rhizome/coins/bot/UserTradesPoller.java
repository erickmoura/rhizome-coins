package hk.rhizome.coins.bot;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.utils.CertHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class UserTradesPoller implements Runnable  {

    protected static final int CORE_POOL_SIZE = 10;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected static KinesisGateway kinesisGateway = new KinesisGateway();

    protected String exchangeId;
    protected TradeService tradeService;
    protected Date lastPolledDate;

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
            TradeHistoryParamsAll params = new TradeHistoryParamsAll();

            params.setCurrencyPairs(CurrencySetService.getCurrencySet());
            params.setEndTime(new Date());
            params.setStartTime(lastPolledDate);


            UserTrades userTrades = tradeService.getTradeHistory(params);

            for(UserTrade trade : userTrades.getUserTrades()){
                //Feed index
                kinesisGateway.sendUserTrade(trade);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(exchangeId + ": Failed to poll User Trades");
            throw(e);
        }
    }


    public UserTradesPoller(Exchange exchange){

        this.exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
        this.tradeService = exchange.getTradeService();

        // Go initially to 1000 days back in time
        long startTime = (new Date().getTime() / 1000) - 24 * 60 * 60 * 1000;
        this.lastPolledDate = new Date(startTime * 1000);

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
