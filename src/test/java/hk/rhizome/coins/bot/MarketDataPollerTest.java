package hk.rhizome.coins.bot;

import org.junit.Test;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.Order;
import com.amazonaws.services.kinesisfirehose.model.*;
import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.RhizomeCoinsConfiguration;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.KinesisGateway;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Arrays;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class MarketDataPollerTest {
	
	@Test
	public void testCollectTicket() throws Exception {
		
		try {
			RhizomeCoinsConfiguration config = getRhizomeCoinsConfiguration();
			ExchangeUtils.getInstance().setExchangeMap(config.getExchanges());
			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
			
			for(String key : config.getExchanges().keySet())
			{
				Exchange exchange = ExchangeUtils.getInstance().getExchange(key);
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
			}
		}catch(Exception ex){
			ex.printStackTrace()	;
			throw ex;
		}	
	}

	@Test
	public void testCollectOrder() throws Exception{
		try {

			RhizomeCoinsConfiguration config = getRhizomeCoinsConfiguration();
			ExchangeUtils.getInstance().setExchangeMap(config.getExchanges());
			CurrencyPair currencyPair = CurrencyPair.XRP_BTC;
			
			for(String key : config.getExchanges().keySet())
			{
				Exchange exchange = ExchangeUtils.getInstance().getExchange(key);
				
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
		if(res == null || res.getRecordId() == null)
			throw new Exception("Error sending the ticket");
		
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

		if(res == null || res.getRecordId() == null)
			throw new Exception("Error sending the ticket");
	}

	
	
	public RhizomeCoinsConfiguration getRhizomeCoinsConfiguration(){
		RhizomeCoinsConfiguration config = new RhizomeCoinsConfiguration();
		config.setExchanges(getExhangesConfiguration());
		config.setLogging(getLogging());
		return config; 
	}

	public Map<String, Map<String, String>> getExhangesConfiguration(){
		Map<String, Map<String, String>> exchanges = new HashMap<String, Map<String, String>>();
		
		Map<String, String> infoExchanges = new HashMap<String, String>();
		infoExchanges.put("name", "ANXPRO");
		infoExchanges.put("key", "e0f2fa0f-0110-454d-99a5-4da24607daf0");
		infoExchanges.put("secret", "/vqfBtEO7k0BburlUkG3Yb47D9LLtiUEJQggKHlO2CwSxTMj2tR2hOSsL6RtVyLxptp/TYiaPL4XwqFGGKisAg==");
		infoExchanges.put("taker", "0.6");
		infoExchanges.put("maker", "0.3");
		exchanges.put("org.knowm.xchange.anx.v2.ANXExchange", infoExchanges);
		return exchanges;
	}

	public Map<String, String> getLogging(){
		Map<String, String> infoLog = new HashMap<String, String>();
		infoLog.put("level", "ERROR");
		
		return infoLog;
	}

}
