package hk.rhizome.coins;

import hk.rhizome.coins.bot.UserTradesPoller;
import hk.rhizome.coins.logger.AppLogger;
import org.knowm.xchange.Exchange;

/**
 * Created by erickmoura on 2/7/2017.
 */
public class UserTradesManager {


    public static final int POLLING_PERIOD = 30; //seconds

    public static void main(String[] args) throws Exception {
        UserTradesManager m = new UserTradesManager();
        m.startUserTradesThreads();
    }

    public void startUserTradesThreads() {

        int i = 0;
        for (Exchange exchange: ExchangeUtils.getInstance().getBotExchanges()) {

            try {
                UserTradesPoller userTradesPoller = new UserTradesPoller(exchange);
                userTradesPoller.startPolling(i, POLLING_PERIOD);
            } catch (Exception e) {
            		AppLogger.getLogger().error("Error in UserTradesManager in startUserTradesThreads : " + e.getLocalizedMessage());
            }
        }
        i++;

    }
}
