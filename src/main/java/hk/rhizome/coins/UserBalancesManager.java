package hk.rhizome.coins;

import hk.rhizome.coins.bot.BalancesPoller;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.UserExchanges;
import java.util.List;


public class UserBalancesManager {

    protected static final int POLLING_RATE = 246060; 
    
    public void startBalancesThreads() {

        List<UserExchanges> userexchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getAllUserExchanges();
        for(UserExchanges ue : userexchanges){
            try {
                BalancesPoller poller = new BalancesPoller(ue, ExchangeUtils.getInstance().getExchange(ue));
                poller.startPolling(0, POLLING_RATE);
            } catch (Exception e) {
                AppLogger.getLogger().error("Error in UserBalancesManager in startBalancesThreads : " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    
}
