package hk.rhizome.coins.db;

import hk.rhizome.coins.logger.AppLogger;

public class DbProxyUtils {

    private static final Object LOCK = new Object();
    
    private static DbProxyUtils singleton;

    private UsersDAOProxy usersProxy;
    private UserExchangesDAOProxy userExchangesProxy;
    private UserBalancesDAOProxy userBalancesProxy;
    private UserOrdersDAOProxy userOrdersProxy;
    private ExchangesDAOProxy exchangesProxy;
    private CoinsDAOProxy coinsProxy;

    public DbProxyUtils(){
    }

    public static DbProxyUtils getInstance() {
        if(singleton == null)
            AppLogger.getLogger().error("DbProxyUtils not initialized");
        return singleton;
    }
    
    public static DbProxyUtils initialize() {
        synchronized (LOCK) {
            if (singleton == null) {
                singleton = new DbProxyUtils();
            }
        }
        return singleton;
    }
 
    public UsersDAOProxy getUsersProxy(){
        return this.usersProxy;
    }
    public void setUsersProxy(UsersDAOProxy proxy){
        this.usersProxy = proxy;
    }
    public UserExchangesDAOProxy getUserExchangesProxy(){
        return this.userExchangesProxy;
    }
    public void setUserExchangesProxy(UserExchangesDAOProxy proxy){
        this.userExchangesProxy = proxy;
    }
    public UserBalancesDAOProxy getUserBalancesProxy(){
        return this.userBalancesProxy;
    }
    public void setUserBalancesProxy(UserBalancesDAOProxy proxy){
        this.userBalancesProxy = proxy;
    }
    public UserOrdersDAOProxy getUserOrdersProxy(){
        return this.userOrdersProxy;
    }
    public void setUserOrdersProxy(UserOrdersDAOProxy proxy){
        this.userOrdersProxy = proxy;
    }
    public ExchangesDAOProxy getExchangesProxy(){
        return this.exchangesProxy;
    }
    public void setExchangesProxy(ExchangesDAOProxy proxy){
        this.exchangesProxy = proxy;
    }
    public CoinsDAOProxy getCoinsProxy(){
        return this.coinsProxy;
    }
    public void setCoinsProxy(CoinsDAOProxy proxy){
        this.coinsProxy = proxy;
    }

   
}
