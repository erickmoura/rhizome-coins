package hk.rhizome.coins.bot;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency; 
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserTrades;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ExchangeUtils.class)
public class UserTradesPollerTest {
    
    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    @Test
    public void sendUserTrade() throws Exception {
        Set<UserTrades> mockedTrades = getMockedUserTrades();
        
        UserExchanges userExchanges = mock(UserExchanges.class, RETURNS_DEEP_STUBS);
        when(userExchanges.getExchange().getXchangeName()).thenReturn("org.knowm.xchange.poloniex.PoloniexExchange");
        TradeService tradeService = mock(TradeService.class, RETURNS_DEEP_STUBS);
        PowerMockito.mockStatic(ExchangeUtils.class );
        Exchange exchange = mock(Exchange.class);
        ExchangeUtils utils = mock(ExchangeUtils.class);
        when(ExchangeUtils.getInstance()).thenReturn(utils); 
        when(ExchangeUtils.getInstance().createXChange(userExchanges)).thenReturn(exchange);
        PowerMockito.when(ExchangeUtils.getInstance().createXChange(userExchanges).getTradeService()).thenReturn(tradeService);

        TradeHistoryParamsAll params = mock(TradeHistoryParamsAll.class);
        List<UserTrade> list = getMockedTrade();
        org.knowm.xchange.dto.trade.UserTrades t = mock(org.knowm.xchange.dto.trade.UserTrades.class);
        when(tradeService.getTradeHistory(params)).thenReturn(t);
        when(tradeService.getTradeHistory(params).getUserTrades()).thenReturn(list);
        Set<UserTrades> set = new HashSet<>();
        
        UserTradesPoller poller = new UserTradesPoller(userExchanges);
        Set<UserTrades> trades = poller.pollManually();

        Assert.assertEquals(trades.size(), 0);
    }

    public Set<UserTrades> getMockedUserTrades(){
        UserTrades t = new UserTrades(1, 1, "3304477", "115369066954132", "BTC/EUR", new BigDecimal("0.00000040625"), "Bitcoin", new BigDecimal("50"),new BigDecimal("0.00000325"),  new Date(), "ASK");
        Set<UserTrades> trades = new HashSet<UserTrades>();
        trades.add(t);
        return trades;
    }

    public List<UserTrade> getMockedTrade(){
        UserTrade t = new UserTrade(OrderType.ASK, new BigDecimal("50"), CurrencyPair.BTC_EUR, new BigDecimal("0.00000325"), new Date(), "3304477", "115369066954132", new BigDecimal("0.00000040625"), Currency.BTC);
        List<UserTrade> trades = new ArrayList<UserTrade>();
        trades.add(t);
        return trades;
    }

    
}
