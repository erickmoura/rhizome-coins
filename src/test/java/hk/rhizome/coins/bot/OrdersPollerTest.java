package hk.rhizome.coins.bot;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ExchangeUtils.class, AccountInfo.class, Wallet.class, Balance.class})
@PowerMockIgnore({ "javax.net.ssl.*", "sun.security.ssl.*" })
public class OrdersPollerTest {
    
    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    @Test
    public void testBalancesPoller() throws Exception {
        
        Set<UserOrders> mockedOrders = getMockedOrders();
        String exchangeId = "Poloniex";
        CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
        
        UserExchanges userExchanges = mock(UserExchanges.class, RETURNS_DEEP_STUBS);
        when(userExchanges.getExchange().getXchangeName()).thenReturn("org.knowm.xchange.poloniex.PoloniexExchange");
        PowerMockito.mockStatic(ExchangeUtils.class );
        Exchange exchange = mock(Exchange.class);
        ExchangeUtils utils = mock(ExchangeUtils.class);
        when(ExchangeUtils.getInstance()).thenReturn(utils); 
        when(ExchangeUtils.getInstance().createXChange(userExchanges)).thenReturn(exchange);
        MarketDataService service = mock(MarketDataService.class, RETURNS_DEEP_STUBS);
        when(exchange.getMarketDataService()).thenReturn(service);
        
        HashMap<String, MarketDataService> dataServices = new HashMap<String, MarketDataService>();
        dataServices.put(exchangeId, service);
        List<LimitOrder> askOrder = new ArrayList<>();
        LimitOrder o1 = new LimitOrder(OrderType.ASK, new BigDecimal(12321), CurrencyPair.BTC_EUR, "1252134523", new Date(), new BigDecimal(1222));
        askOrder.add(o1);
        OrderBook orderBook = new OrderBook(new Date(), askOrder, new ArrayList<LimitOrder>());
        when(dataServices.get(exchangeId).getOrderBook(currencyPair)).thenReturn(orderBook);
        
        OrdersPoller poller = new OrdersPoller(userExchanges, currencyPair);
        Set<UserOrders> userOrders= poller.pollManually();

        Assert.assertEquals(userOrders.size(), 1);
        for(UserOrders u : userOrders){
            Assert.assertEquals(u.getOrderID(), "1252134523");
            Assert.assertEquals(u.getCurrency(), "BTC/EUR");
            Assert.assertEquals(u.getOrderType(), "ASK");
        }
    }

    public Set<UserOrders> getMockedOrders(){
        UserOrders o= new UserOrders("1252134523", 1, 1, "BTC", "ASK", "PENDING_NEW", new BigDecimal(12321), new BigDecimal(12321), new BigDecimal(12321), new Date());
        Set<UserOrders> orders = new HashSet<UserOrders>();
        orders.add(o);
        return orders;
    }    
    
}
