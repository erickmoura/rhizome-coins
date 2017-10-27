package hk.rhizome.coins.bot;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.Order;
import com.amazonaws.services.kinesisfirehose.model.*;
import hk.rhizome.coins.exchanges.CoinMarketCapTicker;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.FeesMatrix;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.marketdata.TradingFeePair;
import hk.rhizome.coins.KinesisGateway;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class MarketDataPollerTest {
	
	@Test
	public void testCollectTicket() throws Exception {
		
		try {
			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
			
			Exchange exchange = getExchangeTest(); 
			String exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
			
			MarketDataService service =  exchange.getMarketDataService();
			// Collect Ticker data
			ExchangeTicker ticker = new ExchangeTicker(exchangeId, service.getTicker(currencyPair));
			System.out.println(ticker);
			
			//getVwap always return null
			String[] nameMethod = {"getCurrencyPair", "getLast", "getBid", "getAsk", "getHigh", "getLow", "getVolume", "getExchange"};
			for(String nameM : nameMethod) {
				Method m = ExchangeTicker.class.getMethod(nameM);
				if (m.invoke(ticker) == null)
						throw new Exception("Field " + nameM  + " is null");
			}
		
		}catch(Exception ex){
			ex.printStackTrace()	;
			throw ex;
		}	
	}

	@Test
	public void testCollectOrder() throws Exception{
		try {

			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
			
			Exchange exchange = getExchangeTest();
			
			MarketDataService service =  exchange.getMarketDataService();
			// Collect Order data
			OrderBook orderBook = service.getOrderBook(currencyPair);
			System.out.println(orderBook);
			
			String[] nameMethod = {"getAsks", "getBids"};
			for(String nameM : nameMethod) {
				Method m = OrderBook.class.getMethod(nameM);
				if (m.invoke(orderBook) == null)
						throw new Exception("Field " + nameM  + " is null");
			}
			
		}catch(Exception ex){
			ex.printStackTrace()	;
			throw ex;
		}
	}

	@Test
	public void testSendTicker() throws Exception {
		
		Ticker ticker = new Ticker.Builder().currencyPair(CurrencyPair.XRP_BTC).last(new BigDecimal("0.00004819")).
		bid(new BigDecimal("0.00004655")).ask(new BigDecimal("0.00005104")).
		high(new BigDecimal("0.00004837")).low(new BigDecimal("0.00004791")).
		volume(new BigDecimal("10699.00000000")).timestamp(new Date()).build();
		ExchangeTicker exchangeTicker = new ExchangeTicker("test-send-ticker", ticker);

		KinesisGateway kinesisGateway = new KinesisGateway();
		kinesisGateway.validateStream();
		PutRecordResult res = kinesisGateway.sendTicker(exchangeTicker);
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.getRecordId());
	}

	@Test
	public void testSendTickers() throws Exception {
		
		CoinMarketCapTicker ticker1 = new CoinMarketCapTicker("test-send-cmc","bitcoin", "Bitcoin", 
																"BTC", 1, new BigDecimal(5318.65), 
																new BigDecimal(1.0), new BigDecimal(2553310000.0),
																new BigDecimal(88392505878.0), new BigDecimal(6619350.0), new BigDecimal(16619350.0), 
																new BigDecimal(0.97), new BigDecimal(9.75), new BigDecimal(22.83), 1507842450L);
		CoinMarketCapTicker ticker2 = new CoinMarketCapTicker("test-send-cmc","ethereum", "Ethereum", 
																"ETH", 2, new BigDecimal(304.136), 
																new BigDecimal(0.0571863), new BigDecimal(478715000.0),
																new BigDecimal(88392505878.0), new BigDecimal(16619350.0), new BigDecimal(16619350.0), 
																new BigDecimal(0.97), new BigDecimal(9.75), new BigDecimal(22.83), 1507842450L);
		List<CoinMarketCapTicker> list = new ArrayList<CoinMarketCapTicker>();
		list.add(ticker1);
		list.add(ticker2);
		KinesisGateway kinesisGateway = new KinesisGateway();
		kinesisGateway.validateStream();
		PutRecordResult res = kinesisGateway.sendTickers(list);
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.getRecordId());
	}

	@Test
	public void testSendMarketDepth() throws Exception {
		
		OrderBook orderBook = new OrderBook(null,
		Arrays.asList(new LimitOrder(Order.OrderType.ASK, new BigDecimal("20.47"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("14.6999")),
			new LimitOrder(Order.OrderType.ASK, new BigDecimal("10.06627287"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("14.7"))),
		Arrays.asList(new LimitOrder(Order.OrderType.BID, new BigDecimal("1.55"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("14.4102")),
			new LimitOrder(Order.OrderType.BID, new BigDecimal("27.77224019"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("14.4101")),
			new LimitOrder(Order.OrderType.BID, new BigDecimal("52669.33019064"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("0"))));
  
		MarketDepth marketDepth = new MarketDepth(new Date(), orderBook);
		marketDepth.setExchange("test-send-market-depth");

		KinesisGateway kinesisGateway = new KinesisGateway();
		kinesisGateway.validateStream();
		PutRecordResult res = kinesisGateway.sendMarketDepth(marketDepth);
		Assert.assertNotNull(res);
		Assert.assertNotNull(res.getRecordId());
		
	}
	
	public Exchange getExchangeTest(){
		ExchangeSpecification spec = new ExchangeSpecification("org.knowm.xchange.anx.v2.ANXExchange");
        spec.setApiKey("key");
        spec.setSecretKey("secret");
        FeesMatrix.setFeesMatrix("org.knowm.xchange.anx.v2.ANXExchange", new TradingFeePair(new BigDecimal(0.3), new BigDecimal(0.6)));
        return ExchangeFactory.INSTANCE.createExchange(spec);
	}
	
}
