package hk.rhizome.coins.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.exchanges.CoinMarketCapService;
import hk.rhizome.coins.exchanges.CoinMarketCapTicker;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;

public class CoinMarketCapPoller  implements Runnable  {

    protected static final int CORE_POOL_SIZE = 50;
    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);

    protected static KinesisGateway kinesisGateway = new KinesisGateway();;
    
    public boolean running;

    public CoinMarketCapPoller(){
        try {
            kinesisGateway.validateStream();
        } catch (Exception e) {
        		AppLogger.getLogger().error("Error in CoinMarketCapPoller in CoinMarketCapPoller : " + e.getLocalizedMessage());
        }
    }

    public void startPolling(long initialDelay, long period) {

        try {
            generic();
            ses.scheduleAtFixedRate(this, initialDelay, period, TimeUnit.SECONDS);
        } catch (Exception e) {
            AppLogger.getLogger().error("Error in CoinMarketCapPoller in startPolling : " + e.getLocalizedMessage());
        }
    }

    public void run() {
        running = true;

        try {
            generic();
        } catch (Exception e) {
        		AppLogger.getLogger().error("Error in CoinMarketCapPoller in run : " + e.getLocalizedMessage());
        }
        running = false;
    }


    private void generic() throws Exception {
        try {

            // Collect Ticker data
            CoinMarketCapService service = new CoinMarketCapService();
            List<CoinMarketCapTicker> list = getFilterList(service.getTickers());
            AppLogger.getLogger().info(list);
            //send to ES
            kinesisGateway.sendTickers(list);

        } catch (Exception e) {
            AppLogger.getLogger().error("Error in CoinMarketCapPoller in generic." + e.getLocalizedMessage());
            e.printStackTrace();
            throw(e);
        }
    }

    private List<CoinMarketCapTicker> getFilterList(List<CoinMarketCapTicker> list){
        List<CoinMarketCapTicker> response = new ArrayList<CoinMarketCapTicker>();

        CoinsSetService coinsService = CoinsSetService.getInstance();
        List<Coins> coins = coinsService.getCoins();
        
        for(CoinMarketCapTicker ticker : list){
            for(Coins c : coins){
                if(c.getName().equals(ticker.getName())){
                    response.add(ticker);
                }
                if(response.size() == coins.size())
                    break;  
            }

        }
        return response;
    }
}
