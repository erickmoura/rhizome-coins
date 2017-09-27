package hk.rhizome.coins.bot;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.account.ExchangeBalance;
import hk.rhizome.coins.marketdata.CurrencySetService;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.utils.CertHelper;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class BalancesPoller implements Runnable  {

    protected static final int CORE_POOL_SIZE = 10;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected static KinesisGateway kinesisGateway = new KinesisGateway();

    protected String exchangeId;
    protected AccountService accountService;
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


            AccountInfo accountInfo = accountService.getAccountInfo();

            for(Currency currency : accountInfo.getWallet().getBalances().keySet()){
                //Feed index
                ExchangeBalance exchangeBalance = new ExchangeBalance(exchangeId, accountInfo.getWallet().getBalances().get(currency));
                kinesisGateway.sendBalance(exchangeBalance);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(exchangeId + ": Failed to poll User Trades");
            throw(e);
        }
    }


    public BalancesPoller(Exchange exchange){

        this.exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
        this.accountService = exchange.getAccountService();

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
