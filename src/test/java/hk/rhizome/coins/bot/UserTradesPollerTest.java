package hk.rhizome.coins.bot;

import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.TradeService;
import com.amazonaws.services.kinesisfirehose.model.*;
import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.RhizomeCoinsConfiguration;
import hk.rhizome.coins.KinesisGateway;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class UserTradesPollerTest {
	
    //@Test
    public void sendUserTrade() throws Exception {

        TradeHistoryParamsAll params = new TradeHistoryParamsAll();
        Set<CurrencyPair> currencyPairs = new HashSet<CurrencyPair>();
        currencyPairs.add(CurrencyPair.BTC_USD);
        
        params.setCurrencyPairs(currencyPairs);
        params.setEndTime(new Date());
        params.setStartTime(new Date());

        RhizomeCoinsConfiguration config = getRhizomeCoinsConfiguration();
        ExchangeUtils.getInstance().setExchangeMap(config.getExchanges());
        
        for(String key : config.getExchanges().keySet())
        {
            Exchange exchange = ExchangeUtils.getInstance().getExchange(key);
            TradeService tradeService = exchange.getTradeService();
    
            UserTrades userTrades = tradeService.getTradeHistory(params);
            for(UserTrade trade : userTrades.getUserTrades()){
                KinesisGateway kinesisGateway = new KinesisGateway();
                kinesisGateway.validateStream();
                PutRecordResult res = kinesisGateway.sendUserTrade(trade);
                if(res == null || res.getRecordId() == null)
                    throw new Exception("Error sending the user trades");
                
            }
        }
        
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
		infoExchanges.put("name", "Poloniex");
		infoExchanges.put("key", "0YLYH5CW-ZFBDX4T6-0V74ZN74-D5BW5LBV");
		infoExchanges.put("secret", "7c565d4e144fdcf8f707ece71a68a377980ceafa6a66757121fefa2a1db8942d4a0a217263808bec0922be571de7835b39c4ba6ebbe1ae005bf642223ee26526");
		infoExchanges.put("taker", "0.6");
		infoExchanges.put("maker", "0.3");
		exchanges.put("org.knowm.xchange.poloniex.PoloniexExchange", infoExchanges);
		return exchanges;
	}

	public Map<String, String> getLogging(){
		Map<String, String> infoLog = new HashMap<String, String>();
		infoLog.put("level", "ERROR");
		
		return infoLog;
	}

}
