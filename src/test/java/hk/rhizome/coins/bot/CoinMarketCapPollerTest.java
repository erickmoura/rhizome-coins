package hk.rhizome.coins.bot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.amazonaws.services.kinesis.model.PutRecordResult;

import static org.mockito.Mockito.*;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.exchanges.CoinMarketCapService;
import hk.rhizome.coins.exchanges.CoinMarketCapTicker;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class CoinMarketCapPollerTest {

    @Before
    public void setUp(){
        AppLogger.initialize();
    }

    @Test
    public void testCollectTicket() throws Exception {
        
        KinesisGateway kinesisGateway = mock(KinesisGateway.class);
        Whitebox.setInternalState(CoinMarketCapPoller.class, "kinesisGateway", kinesisGateway);
        PowerMockito.doNothing().when(kinesisGateway).validateStream();
        
        List<CoinMarketCapTicker> mockedTickers = getMockedTickers();

        CoinMarketCapService service = mock(CoinMarketCapService.class);
        when(service.getTickers()).thenReturn(mockedTickers);
        
        List<Coins> coins = getMockedCoins();
        CoinsSetService coinsService = mock(CoinsSetService.class);
        when(coinsService.getCoins()).thenReturn(coins);
        PutRecordResult r = mock(PutRecordResult.class);
        CoinMarketCapPoller poller  = new CoinMarketCapPoller();
        poller.run();

    }

    public List<Coins> getMockedCoins(){
        List<Coins> coins = new ArrayList<Coins>();
        Coins c1 = new Coins("bitcoin", "Bitcoin", "BTC", new Date(), null);
        Coins c2 = new Coins("litecoin", "Litecoin", "LTC", new Date(), null);
        Coins c3 = new Coins("dash", "Dash", "DASH", new Date(), null);

        coins.add(c1);
        coins.add(c2);
        coins.add(c3);
        return coins;
    }

    public List<CoinMarketCapTicker> getMockedTickers(){
        List<CoinMarketCapTicker> list = new ArrayList<CoinMarketCapTicker>();

        CoinMarketCapTicker t1 = new CoinMarketCapTicker("bitcoin", "Bitcoin", "BTC", 1, new BigDecimal("12"), new BigDecimal("33"), new BigDecimal("0.1"), new BigDecimal("0.0123"), new BigDecimal("12"), new BigDecimal("22"), new BigDecimal("2"), new BigDecimal("222"), new BigDecimal("333"), new Long("524245243"));
        CoinMarketCapTicker t2 = new CoinMarketCapTicker("litecoin", "Litecoin", "LTC", 1, new BigDecimal("12"), new BigDecimal("33"), new BigDecimal("0.1"), new BigDecimal("0.0123"), new BigDecimal("12"), new BigDecimal("22"), new BigDecimal("2"), new BigDecimal("222"), new BigDecimal("333"), new Long("512523525"));
        CoinMarketCapTicker t3 = new CoinMarketCapTicker("lisk", "Lisk", "LSK", 1, new BigDecimal("12"), new BigDecimal("33"), new BigDecimal("0.1"), new BigDecimal("0.0123"), new BigDecimal("12"), new BigDecimal("22"), new BigDecimal("2"), new BigDecimal("222"), new BigDecimal("333"), new Long("4123432"));

        list.add(t1);
        list.add(t2);
        list.add(t3);
        return list;
    }

}
