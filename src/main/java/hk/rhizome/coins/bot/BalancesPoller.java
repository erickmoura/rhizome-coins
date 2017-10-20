package hk.rhizome.coins.bot;

import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.utils.CertHelper;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class BalancesPoller implements Runnable  {

    protected static final int CORE_POOL_SIZE = 10;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected String exchangeName;
    protected UserExchanges userExchanges;
    protected AccountService accountService;
    protected Date startsCollectDate;

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
            Date endDate = new Date();
            Date startDate = userExchanges.getLastUpdatedBalances() == null ? startsCollectDate : userExchanges.getLastUpdatedBalances();
            params.setCurrencyPairs(CurrencySetService.getCurrencySet());
            params.setEndTime(endDate);
            params.setStartTime(startDate);
            
            AccountInfo accountInfo = accountService.getAccountInfo();

            for(Currency currency : accountInfo.getWallet().getBalances().keySet()){
                Balance balance = accountInfo.getWallet().getBalances().get(currency);
                UserBalances userBalances = new UserBalances(userExchanges.getUserID(), userExchanges.getExchangeID(), 
                                        currency.getCurrencyCode(),
                                        balance.getTotal(), balance.getAvailable(), balance.getFrozen(),
                                        balance.getLoaned(), balance.getBorrowed(), balance.getWithdrawing(),
                                        balance.getDepositing());
                
                DbProxyUtils.getInstance().getUserBalancesProxy().create(userBalances);

            }
            userExchanges.setLastUpdatedBalances(endDate);
            DbProxyUtils.getInstance().getUserExchangesProxy().update(userExchanges);
            
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.getLogger().error("Failed to poll balances ", e);
            throw(e);
        }
    }


    public BalancesPoller(UserExchanges userExchanges, Exchange exchange){

        this.userExchanges = userExchanges;
        
        this.exchangeName = exchange.getDefaultExchangeSpecification().getExchangeName();
        this.accountService = exchange.getAccountService();

        // Go initially to 1000 days back in time
        long startTime = (new Date().getTime() / 1000) - 24 * 60 * 60 * 1000;
        this.startsCollectDate = new Date(startTime * 1000);       

        try {
            CertHelper.trustAllCerts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
