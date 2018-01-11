package hk.rhizome.coins.bot;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import hk.rhizome.coins.KinesisGateway;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.marketdata.PricingsMatrix;

@RunWith(PowerMockRunner.class)
public class MarketDataPollerTest {
	
	private AmazonKinesisFirehose kinesisClient;
	private KinesisGateway kinesisGateway;

	@Before
	public void setUp() throws Exception{
		AppLogger.initialize();
	}

	@Test
	public void testMarketDataPoller() throws Exception {
		
		try {
			String exchangeId = "Poloniex";
			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
			Exchange exchange = mock(Exchange.class, RETURNS_DEEP_STUBS);
			ExchangeSpecification spec = mock(ExchangeSpecification.class);
			when(exchange.getDefaultExchangeSpecification()).thenReturn(spec);
			when(exchange.getDefaultExchangeSpecification().getExchangeName()).thenReturn(exchangeId);
			MarketDataService service = mock(MarketDataService.class, RETURNS_DEEP_STUBS);
			when(exchange.getMarketDataService()).thenReturn(service);
			KinesisGateway kinesisGateway = mock(KinesisGateway.class);
			Whitebox.setInternalState(MarketDataPoller.class, "kinesisGateway", kinesisGateway);
			PowerMockito.doNothing().when(kinesisGateway).validateStream();
			
			HashMap<String, MarketDataService> dataServices = new HashMap<String, MarketDataService>();
			Ticker t = new Ticker.Builder().build();
			dataServices.put(exchangeId, service);
			when(dataServices.get(exchangeId).getTicker(currencyPair)).thenReturn(t);
			ExchangeTicker ticker = mock(ExchangeTicker.class);
			PowerMockito.mock(PricingsMatrix.class);
			when(ticker.getTimestamp()).thenReturn(new Date());
			OrderBook orderBook = new OrderBook(new Date(), new ArrayList<LimitOrder>(), new ArrayList<LimitOrder>());
			when(dataServices.get(exchangeId).getOrderBook(currencyPair)).thenReturn(orderBook);
			Date timestamp = new Date();
			MarketDepth marketDepth = new MarketDepth(null, orderBook);
			
			MarketDataPoller poller = new MarketDataPoller(exchange, currencyPair);
			poller.run();

		}catch(Exception ex){
			ex.printStackTrace()	;
			throw ex;
		}	
	}
	
}
