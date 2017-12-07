package hk.rhizome.coins; /**
 * Created by erickmoura on 28/7/2017.
 */

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.LoggerUtils;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.*;
import hk.rhizome.coins.resources.CoinsResources;
import hk.rhizome.coins.resources.ESCoinsResources;
import hk.rhizome.coins.resources.ExchangesResources;
import hk.rhizome.coins.resources.UsersResources;
import hk.rhizome.coins.service.ESCoinsService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hk.rhizome.coins.bot.BalancesPoller;
import hk.rhizome.coins.bot.CoinMarketCapPoller;
import hk.rhizome.coins.db.*;

public class RhizomeCoinsApplication extends Application<RhizomeCoinsConfiguration> {
    
    private HibernateBundle<RhizomeCoinsConfiguration> hibernate;

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
        hibernate = new HibernateBundle<RhizomeCoinsConfiguration>(
                Coins.class, Users.class, Exchanges.class, UserExchanges.class,  
                UserBalances.class, UserOrders.class){
            @Override
            public DataSourceFactory getDataSourceFactory(RhizomeCoinsConfiguration configuration) {
                return DataSourceUtil.getDataSourceFactory(configuration.getDatabase());
            }
        };
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(RhizomeCoinsConfiguration configuration,
                    Environment environment) {
        
        //initialize general configurations                
        AppLogger.initialize(LoggerUtils.getLoggerConfiguration(configuration.getLogging()));
        
        DataSourceUtil.initialize(environment);
        DataSourceFactory dataSourceFactory = DataSourceUtil.getDataSourceFactory(configuration.getDatabase());

        DbProxyUtils.initialize();
        
        //EXCHANGES
        AppLogger.getLogger().info("Get exchanges");
        ExchangesDAO exchangesDAO = new ExchangesDAO(hibernate.getSessionFactory());
        ExchangesDAOProxy exchangesProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(ExchangesDAOProxy.class, ExchangesDAO.class, exchangesDAO);
        DbProxyUtils.getInstance().setExchangesProxy(exchangesProxy);
        ExchangesResources exchangeResources = new ExchangesResources(exchangesDAO);
        try {
            environment.jersey().register(exchangeResources);
        } catch (Exception ex) {
            AppLogger.getLogger().warn("Unable to register ExchangesResources", ex);
        }
        UsersDAO usersDAO = new UsersDAO(hibernate.getSessionFactory());
        UsersDAOProxy usersDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UsersDAOProxy.class, UsersDAO.class, usersDAO);
        UserExchangesDAO userExchangesDAO = new UserExchangesDAO(hibernate.getSessionFactory());
        UserExchangesDAOProxy userExchangesDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserExchangesDAOProxy.class, UserExchangesDAO.class, userExchangesDAO); 
        DbProxyUtils.getInstance().setUsersProxy(usersDAOProxy);
        DbProxyUtils.getInstance().setUserExchangesProxy(userExchangesDAOProxy);
        try {
            ExchangeUtils.getInstance().initialize();
        }catch(Exception ex){
            AppLogger.getLogger().error("Unable to getExchanges " + ex.getLocalizedMessage());
            ex.printStackTrace();
        } 
        
        //COINS
        CoinsDAO coinsDAO = new CoinsDAO(hibernate.getSessionFactory());
        CoinsDAOProxy coinsDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(CoinsDAOProxy.class, CoinsDAO.class, coinsDAO);
        DbProxyUtils.getInstance().setCoinsProxy(coinsDAOProxy);
        try {
            CoinsSetService.getInstance().initialize();
        } catch (Exception ex) {
            AppLogger.getLogger().warn("Unable initialize the coinsDAO : " + ex.getLocalizedMessage());
        }
        try {
            environment.jersey().register(new CoinsResources(coinsDAO));
        } catch (Exception ex) {
            AppLogger.getLogger().warn("Unable to register CoinsResources: " + ex.getLocalizedMessage());
        }

        ESCoinsService esCoinsService = new ESCoinsService(configuration.getElastic());
        ESCoinsResources esCoinsResources = new ESCoinsResources(esCoinsService);
        try {
            environment.jersey().register(esCoinsResources);
        } catch (Exception ex) {
            AppLogger.getLogger().warn("Unable to register ExchangesResources", ex);
        }
        
        //BOTS
        //Start Collection Bots...
        AppLogger.getLogger().info("Start collections bots...");
        MarketDataManager m = new MarketDataManager();
        //m.startCoinMarketPoller();
        //m.startDataMarketThreads();

        //BALANCES
        UserBalancesDAO userBalancesDAO = new UserBalancesDAO(hibernate.getSessionFactory());
        UserBalancesDAOProxy userBalancesDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserBalancesDAOProxy.class, UserBalancesDAO.class, userBalancesDAO);
        DbProxyUtils.getInstance().setUserBalancesProxy(userBalancesDAOProxy);
        UserBalancesManager m1 = new UserBalancesManager();
        //m1.startBalancesThreads();

        //ORDERS
        UserOrdersDAO userOrdersDAO = new UserOrdersDAO(hibernate.getSessionFactory());
        UserOrdersDAOProxy userOrdersDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserOrdersDAOProxy.class, UserOrdersDAO.class, userOrdersDAO);
        DbProxyUtils.getInstance().setUserOrdersProxy(userOrdersDAOProxy);
        UserOrdersManager m2 = new UserOrdersManager();
        //m2.startOrdersThreads();

        //Start UserTrade collection...
        AppLogger.getLogger().info("Start UserTrade collection...");
        UserTradesManager m3 = new UserTradesManager();
        //m3.startUserTradesThreads();

        UsersResources usersResources = new UsersResources(usersDAO, userExchangesDAO);
        try {
            environment.jersey().register(usersResources);
        }
        catch(Exception ex){
            AppLogger.getLogger().error("Unable to register Users Resources " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        
     
    }

}