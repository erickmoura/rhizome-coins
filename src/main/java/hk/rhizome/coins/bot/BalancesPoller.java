package hk.rhizome.coins.bot;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.utils.CertHelper;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;

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
            Set<UserBalances> set = generic();
            User user = userExchanges.getUser();
            user.setBalances(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
            ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;

        try {
            Set<UserBalances> set = generic();
            User user = userExchanges.getUser();
            user.setBalances(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = false;
    }


    private Set<UserBalances> generic() throws Exception {
        try {
            Set<UserBalances> set = new HashSet<UserBalances>();
            
            //TradeHistoryParamsAll params = new TradeHistoryParamsAll();
            Date endDate = new Date();
            //Date startDate = userExchanges.getLastUpdatedBalances() == null ? startsCollectDate : userExchanges.getLastUpdatedBalances();
            //params.setCurrencyPairs(CurrencySetService.getCurrencySet());
            //params.setEndTime(endDate);
            //params.setStartTime(startDate);
            
            AccountInfo accountInfo = accountService.getAccountInfo();
            User user = userExchanges.getUser();
            for(Currency currency : accountInfo.getWallet().getBalances().keySet()){
                Balance balance = accountInfo.getWallet().getBalances().get(currency);
                if(balance.getTotal().compareTo(BigDecimal.ZERO) !=0) {
                    UserBalances userBalances = new UserBalances(user.getID(),userExchanges.getExchange().getID(),
                                            currency.getCurrencyCode(),
                                            balance.getTotal(), balance.getAvailable(), balance.getFrozen(),
                                            balance.getLoaned(), balance.getBorrowed(), balance.getWithdrawing(),
                                            balance.getDepositing(), endDate);
                    set.add(userBalances);
                }
            }
            //userExchanges.setLastUpdatedBalances(endDate);
            //DbProxyUtils.getInstance().getUserExchangesProxy().update(userExchanges);
            
            return set;
            
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.getLogger().error("Failed to poll balances ", e);
            throw(e);
        }
    }

    public Set<UserBalances> pollManually() throws Exception {
        return generic();
    }

    public BalancesPoller(UserExchanges userExchanges){

        this.userExchanges = userExchanges;

        this.exchangeName = userExchanges.getExchange().getXchangeName();
        this.accountService = ExchangeUtils.getInstance().createXChange(userExchanges).getAccountService();

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
