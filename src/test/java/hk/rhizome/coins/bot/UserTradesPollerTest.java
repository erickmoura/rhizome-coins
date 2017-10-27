package hk.rhizome.coins.bot;

import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.TradeService;
import com.amazonaws.services.kinesisfirehose.model.*;
import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.RhizomeCoinsConfiguration;
import hk.rhizome.coins.marketdata.FeesMatrix;
import hk.rhizome.coins.marketdata.TradingFeePair;
import hk.rhizome.coins.KinesisGateway;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class UserTradesPollerTest {
    

    @Test
    public void sendUserTrade() throws Exception {

        TradeHistoryParamsAll params = new TradeHistoryParamsAll();
        Set<CurrencyPair> currencyPairs = new HashSet<CurrencyPair>();
        currencyPairs.add(CurrencyPair.BTC_USD);
        
        params.setCurrencyPairs(currencyPairs);
        params.setEndTime(new Date());
        params.setStartTime(new Date());

        Exchange exchange = getExchangeTest(); 
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
    
   
    public Exchange getExchangeTest(){
		ExchangeSpecification spec = new ExchangeSpecification("org.knowm.xchange.poloniex.PoloniexExchange");
        spec.setApiKey("0YLYH5CW-ZFBDX4T6-0V74ZN74-D5BW5LBV");
        spec.setSecretKey("7c565d4e144fdcf8f707ece71a68a377980ceafa6a66757121fefa2a1db8942d4a0a217263808bec0922be571de7835b39c4ba6ebbe1ae005bf642223ee26526");
        FeesMatrix.setFeesMatrix("org.knowm.xchange.poloniex.PoloniexExchange", new TradingFeePair(new BigDecimal(0.3), new BigDecimal(0.6)));
        return ExchangeFactory.INSTANCE.createExchange(spec);
	}

}
