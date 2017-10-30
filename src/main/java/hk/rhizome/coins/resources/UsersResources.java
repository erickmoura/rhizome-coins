package hk.rhizome.coins.resources;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.bot.BalancesPoller;
import hk.rhizome.coins.bot.OrdersPoller;
import hk.rhizome.coins.db.ExchangesDAO;
import hk.rhizome.coins.db.UserBalancesDAO;
import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UserOrdersDAO;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.utils.ResponseUtils;
import io.dropwizard.hibernate.UnitOfWork;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.knowm.xchange.currency.CurrencyPair;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResources {
    
    UsersDAO usersDAO;
    UserExchangesDAO userExchangesDAO;
    UserBalancesDAO userBalancesDAO;
    UserOrdersDAO userOrdersDAO;
    
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public UsersResources(UsersDAO usersDAO, UserExchangesDAO userExchangesDAO,
			UserBalancesDAO userBalancesDAO, UserOrdersDAO userOrdersDAO) {
		super();
		this.usersDAO = usersDAO;
		this.userExchangesDAO = userExchangesDAO;
		this.userBalancesDAO = userBalancesDAO;
		this.userOrdersDAO = userOrdersDAO;
	}

	@GET
    @UnitOfWork
    @Path("/orders")
    public Map<String, List<UserOrders>> getOrders(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("forceReload") Optional<Object> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getOrders");
        List<UserOrders> orders = null;
        if(forceReload.isPresent() && (Boolean)forceReload.get()){
            List<UserExchanges> userexchanges = userExchangesDAO.findByUserID(1);
            
            for(CurrencyPair currencyPair : CurrencySetService.getCurrencySet()){
                for(UserExchanges ue : userexchanges){
                    try {
                        OrdersPoller poller = new OrdersPoller(ue, ExchangeUtils.getInstance().getExchange(ue), currencyPair);
                        orders = poller.pollManually();
                    } catch (Exception e) {
                        AppLogger.getLogger().error("Error in UsersResources in getOrders : " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date startD = sdf.parse(startDate);
            Date endD = sdf.parse(endDate);
            orders = userOrdersDAO.getOrders(1, startD, endD);
        }
        
        //TODO: just return all orders per exchange, but make the changes to getOrdersResponse as
        //explained there.
        return null;
        //List<UserExchanges> exchanges = userExchangesDAO.findByUserID(1);
        //return orders != null ? ResponseUtils.getOrdersResponse(orders, ResponseUtils.getMapExchanges(exchanges)) : null;
    }

    @GET
    @UnitOfWork
    @Path("/balances")
    public Map<String, List<UserBalances>> getBalances(@QueryParam("collectDate") String collectDate, @QueryParam("forceReload") Optional<Object> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getBalances");
        List<UserBalances> balances = null;
        int userID = 1;
        if(forceReload.isPresent() && (Boolean)forceReload.get()){
            List<UserExchanges> userexchanges = userExchangesDAO.findByUserID(userID);
            for(UserExchanges ue : userexchanges){
                try {
                    BalancesPoller poller = new BalancesPoller(ue, ExchangeUtils.getInstance().getExchange(ue));
                    balances = poller.pollManually();
                } catch (Exception e) {
                    AppLogger.getLogger().error("Error in UserResources in getBalances : " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date collectD = sdf.parse(collectDate);
            balances = userBalancesDAO.findByUserID(userID, collectD);
        }
        
        //TODO: make the appropriate changes to getBalancesResponse()
        //List<Exchanges> exchanges = usersDAO.getExchanges(userID);
        //return balances != null ? ResponseUtils.getBalancesResponse(balances, ResponseUtils.getMapExchanges(exchanges)) : null;
    }
    @GET
    @UnitOfWork
    @Path("/exchanges")
    public List<Exchanges> getExchanges() throws Exception{
        AppLogger.getLogger().debug("Started getExchanges");
        int userID = 1;
        
        //TODO: get used by ID; then use:
        //u.getExchanges()
        //...
        
        
        
        //return usersDAO.userExchangesDAO.findByUserID(userID);
        return null;
    }

}