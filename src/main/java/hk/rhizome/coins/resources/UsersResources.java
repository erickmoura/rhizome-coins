package hk.rhizome.coins.resources;

import hk.rhizome.coins.bot.BalancesPoller;
import hk.rhizome.coins.bot.OrdersPoller;
import hk.rhizome.coins.bot.UserTradesPoller;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.model.UserTrades;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.utils.ResponseUtils;
import io.dropwizard.hibernate.UnitOfWork;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public UsersResources(UsersDAO usersDAO){
		this.usersDAO = usersDAO;
    }
    
	@GET
    @UnitOfWork
    @Path("/orders")
    public Map<String, Set<UserOrders>> getOrders(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("forceReload") Optional<Boolean> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getOrders");
        Set<UserOrders> orders = new HashSet<UserOrders>();;
        User user = usersDAO.getByID(1);
        
        if(forceReload.isPresent() && (Boolean)forceReload.get()){
            
            for(CurrencyPair currencyPair : CurrencySetService.getCurrencySet()){
                for(UserExchanges ue : user.getUserExchanges()){
                    try {
                        OrdersPoller poller = new OrdersPoller(ue, currencyPair);
                        orders.addAll(poller.pollManually());
                    } catch (Exception ex) {
                        AppLogger.getLogger().error("Error in UsersResources in getOrders : " + ex.getLocalizedMessage());
                        ex.printStackTrace();
                    }
                }
            }
            user.setOrders(orders);
            usersDAO.update(user);
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date startD = sdf.parse(startDate);
            Date endD = sdf.parse(endDate);
            orders = user.getOrders();
            //orders = usersDAO.getOrders(user, startD, endD);
        }
        return ResponseUtils.getOrdersResponse(user, orders);
    }
    

    @GET
    @UnitOfWork
    @Path("/balances")
    public Map<String, Set<UserBalances>> getBalances(@QueryParam("collectDate") String collectDate, @QueryParam("forceReload") Optional<Boolean> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getBalances");
        Set<UserBalances> balances = new HashSet<UserBalances>();
        User user = usersDAO.getByID(1);
        if(forceReload.isPresent() && (Boolean)forceReload.get()){
            
            for(UserExchanges ue : user.getUserExchanges()){
                try {
                    BalancesPoller poller = new BalancesPoller(ue);
                    balances.addAll(poller.pollManually());
                } catch (Exception ex) {
                    AppLogger.getLogger().error("Error in UserResources in getBalances : " + ex.getLocalizedMessage());
                    ex.printStackTrace();
                }
            }
            user.setBalances(balances);
            usersDAO.update(user);
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date collectD = sdf.parse(collectDate);
            balances = user.getBalances();
            //balances = userBalancesDAO.findByUserID(userID, collectD);
        }
        return ResponseUtils.getBalancesResponse(user, balances);
    }

    @GET
    @UnitOfWork
    @Path("/trades")
    public Map<String, Set<UserTrades>> getTrades(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("forceReload") Optional<Boolean> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getBalances");
        Set<UserTrades> trades = new HashSet<UserTrades>();
        User user = usersDAO.getByID(1);
        if(forceReload.isPresent() && (Boolean)forceReload.get()){
            
            for(UserExchanges ue : user.getUserExchanges()){
                try {
                    UserTradesPoller poller = new UserTradesPoller(ue);
                    trades.addAll(poller.pollManually());
                } catch (Exception ex) {
                    AppLogger.getLogger().error("Error in UserResources in getBalances : " + ex.getLocalizedMessage());
                    ex.printStackTrace();
                }
            }
            user.setTrades(trades);
            usersDAO.update(user);
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date startD = sdf.parse(startDate);
            trades = user.getTrades();
        }
        return ResponseUtils.getTradesResponse(user, trades);
    }
    
    @GET
    @UnitOfWork
    @Path("/exchanges")
    public Set<Exchanges> getExchanges() throws Exception{
        AppLogger.getLogger().debug("Started getExchanges");
        User u = usersDAO.getByID(1);
        return u.getExchanges();
    }

}