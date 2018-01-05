package hk.rhizome.coins.jobs;

import hk.rhizome.coins.exchanges.CoinMarketCapService;
import hk.rhizome.coins.exchanges.CoinMarketCapTicker;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CoinMarketCapJob extends RhizomeJob {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            AppLogger.getLogger().info("Start job in CoinMarketCapJob.");
            
            // Collect Ticker data
            CoinMarketCapService service = new CoinMarketCapService();
            List<CoinMarketCapTicker> list = getFilterList(service.getTickers());
            AppLogger.getLogger().debug(list);
            //send to ES
            kinesisGateway.sendTickers(list);
            
            AppLogger.getLogger().info("End job in CoinMarketCapJob.");
            
        } catch (Exception e) {
            AppLogger.getLogger().error("Error in CoinMarketCapPoller in generic." + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private List<CoinMarketCapTicker> getFilterList(List<CoinMarketCapTicker> list){
        List<CoinMarketCapTicker> response = new ArrayList<CoinMarketCapTicker>();
        List<Coins> coins = CoinsSetService.getInstance().getCoins();
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
