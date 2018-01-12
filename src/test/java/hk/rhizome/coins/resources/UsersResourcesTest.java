package hk.rhizome.coins.resources;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hk.rhizome.coins.db.UserExchangesDAO;
import hk.rhizome.coins.db.UsersDAO;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.model.UserTrades;

public class UsersResourcesTest {
    
    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    //@Test
    public void getBalancesNoForceReload() throws Exception {
        String collect = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Set<Exchanges> mockedExchanges = getMockedExchanges();
        Set<UserBalances> mockedBalances = getMockedUserBalances();
        
        User user = mock(User.class);
        when(user.getExchanges()).thenReturn(mockedExchanges);
        UsersDAO usersDAO = mock(UsersDAO.class);
        when(usersDAO.getByID(1)).thenReturn(user);
        
        UsersResources resources = new UsersResources(usersDAO);
        when(user.getBalances()).thenReturn(mockedBalances);
        
        Map<String, Set<UserBalances>> data = resources.getBalances(collect,Optional.empty());
        Assert.assertEquals(data.keySet().size(), 1);
        Assert.assertEquals(mockedBalances, data.get("Poloniex"));
        Assert.assertEquals(data.get("Poloniex").size(), 1);
    }

    @Test
    public void getOrdersNoForceReload() throws Exception {
        String start = "2017-10-23 10:00:00";
        String end = "2017-10-23 10:00:00";
        Set<Exchanges> mockedExchanges = getMockedExchanges();
        Set<UserOrders> mockedOrders = getMockedUserOrders();
        
        User user = mock(User.class);
        when(user.getExchanges()).thenReturn(mockedExchanges);
        UsersDAO usersDAO = mock(UsersDAO.class);
        when(usersDAO.getByID(1)).thenReturn(user);
        
        UsersResources resources = new UsersResources(usersDAO);
        when(user.getOrders()).thenReturn(mockedOrders);
        
        Map<String, Set<UserOrders>> data = resources.getOrders(start, end, Optional.empty());
        Assert.assertEquals(data.keySet().size(), 1);
        Assert.assertEquals(mockedOrders, data.get("Poloniex"));
        Assert.assertEquals(data.get("Poloniex").size(), 2);
    }

    @Test
    public void getUserTradesNoForceLoad() throws Exception {

        String start = "2017-10-23 10:00:00";
        String end = "2017-10-23 10:00:00";
        
        Set<Exchanges> mockedExchanges = getMockedExchanges();
        Set<UserTrades> mockedTrades = getMockedUserTrades();
        
        User user = mock(User.class);
        when(user.getExchanges()).thenReturn(mockedExchanges);
        UsersDAO usersDAO = mock(UsersDAO.class);
        when(usersDAO.getByID(1)).thenReturn(user);
        
        UsersResources resources = new UsersResources(usersDAO);
        when(user.getTrades()).thenReturn(mockedTrades);
        
        Map<String, Set<UserTrades>> data = resources.getTrades(start, end, Optional.empty());
        Assert.assertEquals(data.keySet().size(), 1);
        Assert.assertEquals(data.get("Poloniex"), mockedTrades);
        Assert.assertEquals(data.get("Poloniex").size(), 1);
        System.out.println(data);
    }

    @Test
    public void getExchanges() throws Exception {
        Set<Exchanges> mockedSet = getMockedExchanges();
        
        User u = mock(User.class);
        UsersDAO usersDAO = mock(UsersDAO.class);
        when(usersDAO.getByID(1)).thenReturn(u);
        when(u.getExchanges()).thenReturn(mockedSet);
        
        UsersResources resources = new UsersResources(usersDAO);
        Set<Exchanges> exchanges = resources.getExchanges();

        Assert.assertEquals(mockedSet, exchanges);
    }

    public Set<Exchanges> getMockedExchanges(){
        Set<Exchanges> exchanges = new HashSet<Exchanges>();
        Exchanges ex1 = new Exchanges(1, "Poloniex", "org.knowm.xchange.poloniex.PoloniexExchange",	new BigDecimal("0.25"), new BigDecimal("0.15"),	20);
        exchanges.add(ex1);
        return exchanges;
    }

    public Set<UserOrders> getMockedUserOrders() throws Exception{
        String start = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(start);

        Set<UserOrders> orders = new HashSet<UserOrders>();
        UserOrders o1 = new UserOrders("12", 1, 1, "ETH/BTC", "ASK", "PENDING_NEW", new BigDecimal("222.22"), BigDecimal.ZERO, BigDecimal.ZERO, d);
        UserOrders o2 = new UserOrders("34", 1, 1, "ETH/BTC", "BID", "PENDING_NEW", new BigDecimal("262.00"), BigDecimal.ZERO, BigDecimal.ZERO, d);
        orders.add(o1);
        orders.add(o2);
        return orders;
    }

    public Set<UserBalances> getMockedUserBalances() throws Exception{
        String start = "2017-10-23 10:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(start);

        Set<UserBalances> balances = new HashSet<UserBalances>();
        UserBalances b1 = new UserBalances(1, 1, "ETH/BTC", new BigDecimal("2322.23"), new BigDecimal("2322.23"), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, d);
        balances.add(b1);
        return balances;
    }

    public Set<UserTrades> getMockedUserTrades(){
        UserTrades t = new UserTrades(1, 1, "3304477", "115369066954132", "SC/BTC", new BigDecimal("0.00000040625"), "Bitcoin", new BigDecimal("50"),new BigDecimal("0.00000325"),  new Date(), "ASK");
        Set<UserTrades> trades = new HashSet<UserTrades>();
        trades.add(t);
        return trades;
    }

   
}
