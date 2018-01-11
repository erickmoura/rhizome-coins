package hk.rhizome.coins.bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.service.account.AccountService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ExchangeUtils.class, AccountInfo.class, Wallet.class, Balance.class})
@PowerMockIgnore({ "javax.net.ssl.*", "sun.security.ssl.*" })
public class BalancesPollerTest {
    
    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    @Test
    public void testBalancesPoller() throws Exception {
        Set<UserBalances> mockedBalances = getMockedBalances();
        
        UserExchanges userExchanges = mock(UserExchanges.class, RETURNS_DEEP_STUBS);
        when(userExchanges.getExchange().getXchangeName()).thenReturn("org.knowm.xchange.poloniex.PoloniexExchange");
        PowerMockito.mockStatic(ExchangeUtils.class );
        Exchange exchange = mock(Exchange.class);
        ExchangeUtils utils = mock(ExchangeUtils.class);
        when(ExchangeUtils.getInstance()).thenReturn(utils); 
        when(ExchangeUtils.getInstance().createXChange(userExchanges)).thenReturn(exchange);
        AccountService accountService = mock(AccountService.class);
        PowerMockito.when(ExchangeUtils.getInstance().createXChange(userExchanges).getAccountService()).thenReturn(accountService);

        AccountInfo accountInfo = PowerMockito.mock(AccountInfo.class, RETURNS_DEEP_STUBS);
        when(accountService.getAccountInfo()).thenReturn(accountInfo);
        User user = mock(User.class);
        when(userExchanges.getUser()).thenReturn(user);
        Wallet wallet = PowerMockito.mock(Wallet.class, RETURNS_DEEP_STUBS);
        when(accountInfo.getWallet()).thenReturn(wallet);
        Map<Currency, Balance> map = getMockedCurencyBalance();
        when(accountInfo.getWallet().getBalances()).thenReturn(map);
        Currency currency = Currency.BTC;
        Balance b = PowerMockito.mock(Balance.class, RETURNS_DEEP_STUBS);
        
        BalancesPoller poller = new BalancesPoller(userExchanges);
        Set<UserBalances> userBalances= poller.pollManually();

        Assert.assertEquals(userBalances.size(), 1);
        for(UserBalances u : userBalances){
            Assert.assertEquals(u.getCurrency(), "BTC");
            Assert.assertEquals(u.getTotal(), new BigDecimal(333));
        }
    }

    public Set<UserBalances> getMockedBalances(){
        UserBalances b = new UserBalances(1, 1, "BTC", new BigDecimal(333), new BigDecimal(200),  new BigDecimal(133), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new Date());
        Set<UserBalances> balances = new HashSet<UserBalances>();
        balances.add(b);
        return balances;
    }

    public Map<Currency, Balance> getMockedCurencyBalance(){
        Balance b = new Balance(Currency.BTC, new BigDecimal(333), new BigDecimal(200),  new BigDecimal(133));
        Map<Currency, Balance> map = new HashMap<Currency, Balance>();
        map.put(Currency.BTC, b);
        return map;
    }

    
}
