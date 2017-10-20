package hk.rhizome.coins;

import hk.rhizome.coins.bot.OrdersPoller;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.UserExchanges;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;


public class UserOrdersManager {

    protected static final int POLLING_RATE = 246060; 
    
    public void startOrdersThreads() {

        int i = 0;
        List<UserExchanges> userexchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getAllUserExchanges();
        
        for(CurrencyPair currencyPair : CurrencySetService.getCurrencySet())
        {
            for(UserExchanges ue : userexchanges){
                try {
                    OrdersPoller poller = new OrdersPoller(ue, ExchangeUtils.getInstance().getExchange(ue), currencyPair);
                    poller.startPolling(i, POLLING_RATE);
                } catch (Exception e) {
                    AppLogger.getLogger().error("Error in UserOrdersManager in startOrdersThreads : " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
            i++;
        }
        
    }

    
}
