package hk.rhizome.coins.resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UsersResourcesTest {
    
    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    @Test
    public void getBalancesNoForceReload() throws Exception {
        int userID = 1;
        String collect = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date collectD = sdf.parse(collect);
        List<Exchanges> mockedExchanges = getMockedExchanges();
        List<UserBalances> mockedBalances = getMockedUserBalances();
        
        UsersDAO usersDAO = mock(UsersDAO.class);
        UserExchangesDAO usersExchangesDAO = mock(UserExchangesDAO.class);
        when(usersDAO.getExchanges(userID)).thenReturn(mockedExchanges);
        
        UsersResources resources = new UsersResources(usersDAO, usersExchangesDAO);
        when(usersDAO.getBalances(userID, collectD)).thenReturn(mockedBalances);
        
        Map<String, List<UserBalances>> data = resources.getBalances(collect,Optional.empty());
        Assert.assertEquals(data.keySet().size(), 1);
        Assert.assertEquals(mockedBalances, data.get("Poloniex"));
        Assert.assertEquals(data.get("Poloniex").size(), 1);
    }

    @Test
    public void getOrdersNoForceReload() throws Exception {
        int userID = 1;
        String start = "2017-10-23 10:00:00";
        String end = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);
        List<Exchanges> mockedExchanges = getMockedExchanges();
        List<UserOrders> mockedOrders = getMockedUserOrders();
        
        UsersDAO usersDAO = mock(UsersDAO.class);
        UserExchangesDAO usersExchangesDAO = mock(UserExchangesDAO.class);
        when(usersDAO.getExchanges(userID)).thenReturn(mockedExchanges);
        
        UsersResources resources = new UsersResources(usersDAO, usersExchangesDAO);
        when(usersDAO.getOrders(userID, startDate, endDate)).thenReturn(mockedOrders);
        
        Map<String, List<UserOrders>> data = resources.getOrders(start, end, Optional.empty());
        Assert.assertEquals(data.keySet().size(), 1);
        Assert.assertEquals(mockedOrders, data.get("Poloniex"));
        Assert.assertEquals(data.get("Poloniex").size(), 2);
    }

    @Test
    public void getExchanges() throws Exception {
        int userID = 1;
        List<Exchanges> mockedList = getMockedExchanges();
        
        UsersDAO usersDAO = mock(UsersDAO.class);
        UserExchangesDAO usersExchangesDAO = mock(UserExchangesDAO.class);
        when(usersDAO.getExchanges(userID)).thenReturn(mockedList);
        
        UsersResources resources = new UsersResources(usersDAO, usersExchangesDAO);
        List<Exchanges> exchanges = resources.getExchanges();

        Assert.assertEquals(mockedList, exchanges);
    }

    public List<Exchanges> getMockedExchanges(){
        List<Exchanges> exchanges = new ArrayList<Exchanges>();
        Exchanges ex1 = new Exchanges(1, "Poloniex", "org.knowm.xchange.poloniex.PoloniexExchange",	new BigDecimal(0.25), new BigDecimal(0.15),	20);
        exchanges.add(ex1);
        return exchanges;
    }

    public List<UserOrders> getMockedUserOrders() throws Exception{
        String start = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(start);

        List<UserOrders> orders = new ArrayList<UserOrders>();
        UserOrders o1 = new UserOrders("12", 1, 1, "ETH/BTC", "ASK", "PENDING_NEW", new BigDecimal(222.22), BigDecimal.ZERO, BigDecimal.ZERO, d);
        UserOrders o2 = new UserOrders("34", 1, 1, "ETH/BTC", "BID", "PENDING_NEW", new BigDecimal(262.00), BigDecimal.ZERO, BigDecimal.ZERO, d);
        orders.add(o1);
        orders.add(o2);
        return orders;
    }

    public List<UserBalances> getMockedUserBalances() throws Exception{
        String start = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(start);

        List<UserBalances> balances = new ArrayList<UserBalances>();
        UserBalances b1 = new UserBalances(1, 1, "ETH/BTC", new BigDecimal(2322.23), new BigDecimal(2322.23), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, d);
        balances.add(b1);
        return balances;
    }

   
}
