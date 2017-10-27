package hk.rhizome.coins.resources;

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
import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.bot.BalancesPoller;
import hk.rhizome.coins.bot.OrdersPoller;
import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.utils.ResponseUtils;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResources {
    
    UsersDAO usersDAO;
    UserExchangesDAO userExchangesDAO;
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public UsersResources(UsersDAO usersDAO, UserExchangesDAO userExchangesDAO){
        this.usersDAO = usersDAO;
        this.userExchangesDAO  = userExchangesDAO;
    }

    @GET
    @UnitOfWork
    @Path("/orders")
    public Map<String, List<UserOrders>> getOrders(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("forceReload") Optional<Boolean> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getOrders");
        List<UserOrders> orders = null;
        if(forceReload.isPresent() && forceReload.get()){
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
            orders = usersDAO.getOrders(1, startD, endD);
        }
        List<Exchanges> exchanges = usersDAO.getExchanges(1);
        return orders != null ? ResponseUtils.getOrdersResponse(orders, ResponseUtils.getMapExchanges(exchanges)) : null;
    }

    @GET
    @UnitOfWork
    @Path("/balances")
    public Map<String, List<UserBalances>> getBalances(@QueryParam("collectDate") String collectDate, @QueryParam("forceReload") Optional<Boolean> forceReload) throws Exception{
        AppLogger.getLogger().debug("Started getBalances");
        List<UserBalances> balances = null;
        int userID = 1;
        if(forceReload.isPresent() && forceReload.get()){
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
            balances = usersDAO.getBalances(userID, collectD);
        }
        List<Exchanges> exchanges = usersDAO.getExchanges(userID);
        return balances != null ? ResponseUtils.getBalancesResponse(balances, ResponseUtils.getMapExchanges(exchanges)) : null;
    }
    @GET
    @UnitOfWork
    @Path("/exchanges")
    public List<Exchanges> getExchanges() throws Exception{
        AppLogger.getLogger().debug("Started getExchanges");
        int userID = 1;
        return usersDAO.getExchanges(userID);
    }

}