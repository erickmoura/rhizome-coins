package hk.rhizome.coins.bot;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserTrades;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.utils.CertHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
    protected String exchangeName;
    protected UserExchanges userExchanges;
    
    public boolean running;

    public void startPolling(long initialDelay, long period) {

        try {
            Set<UserTrades> set = generic();
            User user = userExchanges.getUser();
            user.setTrades(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
            ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            AppLogger.getLogger().error("Error in UserTradesPoller in startPolling : " + e.getLocalizedMessage());
        }
    }

    public void run() {
        running = true;

        try {
            Set<UserTrades> set = generic();
            User user = userExchanges.getUser();
            user.setTrades(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
        } catch (Exception e) {
        		AppLogger.getLogger().error("Error in UserTradesPoller in run : " + e.getLocalizedMessage());
        }
        running = false;
    }


    private Set<UserTrades> generic() throws Exception {
        try {   
            TradeHistoryParamsAll params = new TradeHistoryParamsAll();

            params.setCurrencyPairs(CurrencySetService.getCurrencySet());
            params.setEndTime(new Date());
            params.setStartTime(lastPolledDate);

            Set<UserTrades> set = new HashSet<UserTrades>();
            for(UserTrade trade : tradeService.getTradeHistory(params).getUserTrades()){
                UserTrades t = new UserTrades(userExchanges.getUser().getID(), userExchanges.getExchange().getID(), 
                                            trade.getId(), trade.getOrderId(), trade.getCurrencyPair().toString(),
                                            trade.getFeeAmount(), trade.getFeeCurrency().getDisplayName(), trade.getTradableAmount(), 
                                            trade.getPrice(), trade.getTimestamp(), trade.getType().toString());
                set.add(t);
            }
            return set;

        } catch (Exception e) {
                AppLogger.getLogger().error("Error in UserTradesPoller in generic : " + exchangeName + ": Failed to poll User Trades");
                e.printStackTrace();
            throw(e);
        }
    }

    public Set<UserTrades> pollManually() throws Exception {
        return generic();
    }


    public UserTradesPoller(UserExchanges userExchanges){

        this.userExchanges = userExchanges;
        
        this.exchangeName = userExchanges.getExchange().getXchangeName();
        this.tradeService = ExchangeUtils.getInstance().createXChange(userExchanges).getTradeService();
        
        // Go initially to 1000 days back in time
        long startTime = (new Date().getTime() / 1000) - 24 * 60 * 60 * 1000;
        this.lastPolledDate = new Date(startTime * 1000);

        try {
            kinesisGateway.validateStream();
        } catch (Exception e) {
        		AppLogger.getLogger().error("Error in UserTradesPoller in UserTradesPoller : " + e.getLocalizedMessage());
        }

        try {
            CertHelper.trustAllCerts();
        } catch (Exception e) {
            AppLogger.getLogger().error("Error in UserTradesPoller in UserTradesPoller : " + e.getLocalizedMessage());
        }
    }
}
