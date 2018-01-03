package hk.rhizome.coins.bot;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.CertHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class OrdersPoller implements Runnable  {

    protected static final int CORE_POOL_SIZE = 10;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected String exchangeName;
    protected Date startsCollectDate;
    protected UserExchanges userExchanges;
    protected static HashMap<String, MarketDataService> dataServices = new HashMap<String, MarketDataService>();

    protected CurrencyPair currencyPair;

    public boolean running;

    public void startPolling(long initialDelay, long period) {

        try {
            Set<UserOrders> set = generic();
            User user = userExchanges.getUser();
            user.setOrders(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
            ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        running = true;
        try {
            Set<UserOrders> set = generic();
            User user = userExchanges.getUser();
            user.setOrders(set);
            DbProxyUtils.getInstance().getUsersProxy().saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        running = false;
    }


    private Set<UserOrders> generic() throws Exception {
        try {
            Set<UserOrders> set = new HashSet<UserOrders>();
            
            OrderBook orderBook = dataServices.get(exchangeName).getOrderBook(currencyPair);
            for(LimitOrder order : orderBook.getAsks()){
                if(order.getId() != null){
                    UserOrders o = new UserOrders(order.getId(), userExchanges.getUser().getID(), userExchanges.getExchange().getID(),
                                                order.getCurrencyPair().toString(),order.getType().toString(),
                                                order.getStatus().toString(), order.getTradableAmount(),
                                                order.getCumulativeAmount(), order.getAveragePrice(), order.getTimestamp());
                    set.add(o);
                }
            }
            for(LimitOrder order : orderBook.getBids()){
                if(order.getId() != null){
                    UserOrders o = new UserOrders(order.getId(), userExchanges.getUser().getID(), userExchanges.getExchange().getID(),
                                                order.getCurrencyPair().toString(),order.getType().toString(),
                                                order.getStatus().toString(), order.getTradableAmount(),
                                                order.getCumulativeAmount(), order.getAveragePrice(), order.getTimestamp());
                    set.add(o);
                }
            }
            return set;
            
        } catch (Exception e) {
            e.printStackTrace();
            AppLogger.getLogger().error("Failed to poll orders ", e);
            throw(e);
        }
    }

    public Set<UserOrders> pollManually() throws Exception {
        return generic();
    }


    public OrdersPoller(UserExchanges userExchanges, CurrencyPair currencyPair){
        
        this.userExchanges = userExchanges;
        this.exchangeName = userExchanges.getExchange().getXchangeName();
        this.currencyPair = currencyPair;

        if(dataServices.get(exchangeName) == null) {
            dataServices.put(exchangeName, ExchangeUtils.getInstance().createXChange(userExchanges).getMarketDataService());
        }
        
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
