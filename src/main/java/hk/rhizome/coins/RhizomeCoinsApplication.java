package hk.rhizome.coins; /**
 * Created by erickmoura on 28/7/2017.
 */

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.LoggerUtils;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.resources.ExchangesResources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.db.DataSourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hk.rhizome.coins.bot.CoinMarketCapPoller;
import hk.rhizome.coins.db.DataSourceUtil;

public class RhizomeCoinsApplication extends Application<RhizomeCoinsConfiguration> {
    public static void main(String[] args) throws Exception {
        new RhizomeCoinsApplication().run(args);
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public void initialize(Bootstrap<RhizomeCoinsConfiguration> bootstrap) {
    		//migrations
    		bootstrap.addBundle(new MigrationsBundle<RhizomeCoinsConfiguration>() {
            @Override
                public DataSourceFactory getDataSourceFactory(RhizomeCoinsConfiguration configuration) {
                    return DataSourceUtil.getDataSourceFactory(configuration.getDatabase());
                }
        });
    }

    @Override
    public void run(RhizomeCoinsConfiguration configuration,
                    Environment environment) {
        
        //initialize general configurations                
        AppLogger.initialize(LoggerUtils.getLoggerConfiguration(configuration.getLogging()));
        DataSourceUtil.initialize(environment);
                       
        DataSourceFactory dataSourceFactory = DataSourceUtil.getDataSourceFactory(configuration.getDatabase());
        
        ExchangesResources exchangeResources = new ExchangesResources(dataSourceFactory);
        // create exchanges resource
        try {
            environment.jersey().register(exchangeResources);
        } catch (Exception ex) {
            AppLogger.getLogger().warn("Unable to register ExchangesResources", ex);
        }
        
        //get Exchanges for user bot
        AppLogger.getLogger().info("Get exchanges");
        List<Map<String, Object>> exchanges = new ArrayList<>();
        try{
            exchanges = exchangeResources.getExchanges();
            ExchangeUtils.getInstance().setExchanges(exchanges);
        }catch(Exception ex){
            ex.printStackTrace();
            AppLogger.getLogger().warn("Unable to getExchanges " + ex.getLocalizedMessage());
        } 

        //get coins
        AppLogger.getLogger().info("Get coins");
        try {
            CoinsSetService.getInstance().initialize(DataSourceUtil.getDataSourceFactory(configuration.getDatabase()));
        } catch (Exception ex) {
            AppLogger.getLogger().error("Unable to retrieve Coins " +  ex.getLocalizedMessage()); 
        }
        
        //Start Collection Bots...
        AppLogger.getLogger().info("Start collections bots...");
        MarketDataManager m = new MarketDataManager();
        m.startCoinMarketPoller();
        m.startDataMarketThreads();
        
        //Start UserTrade collection...
        AppLogger.getLogger().info("Start UserTrade collection...");
        UserTradesManager m1 = new UserTradesManager();
        m1.startUserTradesThreads();
        
     
    }

}