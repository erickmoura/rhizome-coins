package hk.rhizome.coins; /**
 * Created by erickmoura on 28/7/2017.
 */

import hk.rhizome.coins.db.CoinsDAO;
import hk.rhizome.coins.db.CoinsDAOProxy;
import hk.rhizome.coins.db.DataSourceUtil;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.db.ExchangesDAO;
import hk.rhizome.coins.db.ExchangesDAOProxy;
import hk.rhizome.coins.db.UserBalancesDAO;
import hk.rhizome.coins.db.UserBalancesDAOProxy;
import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UserExchangesDAOProxy;
import hk.rhizome.coins.db.UserOrdersDAO;
import hk.rhizome.coins.db.UserOrdersDAOProxy;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.db.UsersDAOProxy;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.LoggerUtils;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.model.UserTrades;
import hk.rhizome.coins.resources.CoinsResources;
import hk.rhizome.coins.resources.ExchangesResources;
import hk.rhizome.coins.resources.UsersResources;
import io.dropwizard.Application; 
import io.dropwizard.setup.Bootstrap; 
import io.dropwizard.setup.Environment; 
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;


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
            public DataSourceFactory getDataSourceFactory(RhizomeCoinsConfiguration configuration) {
                    return DataSourceUtil.getDataSourceFactory(configuration.getDatabase());
                }
        });
        hibernate = new HibernateBundle<RhizomeCoinsConfiguration>(
                Coins.class, User.class, Exchanges.class, UserExchanges.class,  
                UserBalances.class, UserOrders.class, UserTrades.class){
    
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
        
        //JOBS 
        JobManager jobsManager = new JobManager();
        jobsManager.initializeJobs();
        jobsManager.runJobs();

        //BOTS
        //Start Collection Bots...
        AppLogger.getLogger().info("Start collections bots...");
        MarketDataManager marketDataManager = new MarketDataManager();
        //marketDataManager.startCoinMarketPoller();
        //marketDataManager.startDataMarketThreads();

        //BALANCES
        UserBalancesDAO userBalancesDAO = new UserBalancesDAO(hibernate.getSessionFactory());
        UserBalancesDAOProxy userBalancesDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserBalancesDAOProxy.class, UserBalancesDAO.class, userBalancesDAO);
        DbProxyUtils.getInstance().setUserBalancesProxy(userBalancesDAOProxy);
        UserBalancesManager userBalancesManager = new UserBalancesManager();
        //userBalancesManager.startBalancesThreads();

        //ORDERS
        UserOrdersDAO userOrdersDAO = new UserOrdersDAO(hibernate.getSessionFactory());
        UserOrdersDAOProxy userOrdersDAOProxy = new UnitOfWorkAwareProxyFactory(hibernate).create(UserOrdersDAOProxy.class, UserOrdersDAO.class, userOrdersDAO);
        DbProxyUtils.getInstance().setUserOrdersProxy(userOrdersDAOProxy);
        UserOrdersManager userOrdersManager = new UserOrdersManager();
        //userOrdersManager.startOrdersThreads();

        //Start UserTrade collection...
        AppLogger.getLogger().info("Start UserTrade collection...");
        UserTradesManager userTradesManager = new UserTradesManager();
        //userTradesManager.startUserTradesThreads();

        UsersResources usersResources = new UsersResources(usersDAO);
        try {
            environment.jersey().register(usersResources);
        }
        catch(Exception ex){
            AppLogger.getLogger().error("Unable to register Users Resources " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        
     
    }

}