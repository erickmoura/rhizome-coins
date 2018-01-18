package hk.rhizome.coins.bot;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.powermock.modules.junit4.PowerMockRunner;

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;

@RunWith(PowerMockRunner.class)
public class MarketDataPollerTest {

	@Before
	public void setUp() throws Exception{
		AppLogger.initialize();
	}

	@Test
	public void testMarketDataPoller() throws Exception {
		
		try {
			String namespace = "org.knowm.xchange.poloniex.PoloniexExchange";
			String exchangeId = "Poloniex";
			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;

			ExchangeSpecification exSpec = new ExchangeSpecification(namespace);
			exSpec.setApiKey("key");
			exSpec.setSecretKey("secret");
			
			MarketDataService service = mock(MarketDataService.class);
			Ticker t = new Ticker.Builder().bid(new BigDecimal("12")).currencyPair(currencyPair).last(new BigDecimal("22")).build();
			when(service.getTicker(currencyPair)).thenReturn(t);
			//get ticker
			ExchangeTicker ticker = new ExchangeTicker(exchangeId, service.getTicker(currencyPair));
			Assert.assertEquals(ticker.getBid(), new BigDecimal("12"));
			Assert.assertEquals(ticker.getExchange(), exchangeId);
			Assert.assertEquals(ticker.getCurrencyPair(), currencyPair);
			
		}catch(Exception ex){
			ex.printStackTrace()	;
			throw ex;
		}	
	}
	
}
