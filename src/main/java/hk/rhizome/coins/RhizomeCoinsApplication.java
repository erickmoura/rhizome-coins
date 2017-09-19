package hk.rhizome.coins; /**
 * Created by erickmoura on 28/7/2017.
 */

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.db.DataSourceFactory;

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

        ExchangeUtils.getInstance().setExchangeMap(configuration.getExchanges());

        //Start Collection Bots...
        MarketDataManager m = new MarketDataManager();
        m.startDataMarketThreads();

        //Start UserTrade collection...
        UserTradesManager m1 = new UserTradesManager();
        m1.startUserTradesThreads();
    }

}
