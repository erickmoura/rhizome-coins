package hk.rhizome.coins.bot;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.meta.ExchangeMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.jobs.XChangeJob;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.marketdata.PricingsMatrix;
import si.mazi.rescu.SynchronizedValueFactory;

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
			Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exSpec);
          
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
