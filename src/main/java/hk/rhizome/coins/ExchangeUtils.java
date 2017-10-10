package hk.rhizome.coins;

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.FeesMatrix;
import hk.rhizome.coins.marketdata.TradingFeePair;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExchangeUtils {

  private static ExchangeUtils singleton;
  private Map<String, Exchange> exchangeSpecificationMap;

  private ExchangeUtils(){
    exchangeSpecificationMap = new HashMap<String,Exchange>();
  }

  public static ExchangeUtils getInstance(){
    if (singleton == null){
      singleton = new ExchangeUtils();

    }
    return singleton;
  }

  public void setExchangeMap(Map<String, Map<String, String>> exchangeMap){
    for(String exchangeClassName : exchangeMap.keySet()){
      ExchangeSpecification spec = new ExchangeSpecification(exchangeClassName);

      spec.setApiKey(exchangeMap.get(exchangeClassName).get("key"));
      spec.setSecretKey(exchangeMap.get(exchangeClassName).get("secret"));

      FeesMatrix.setFeesMatrix(
              exchangeMap.get(exchangeClassName).get("name"),
              new TradingFeePair(
                      new BigDecimal(exchangeMap.get(exchangeClassName).get("maker")),
                      new BigDecimal(exchangeMap.get(exchangeClassName).get("taker")))
              );

      try{
        exchangeSpecificationMap.put(exchangeClassName, ExchangeFactory.INSTANCE.createExchange(spec));
      }
      catch(Exception e)
      {
        AppLogger.getLogger().error("Exception in KinesisGateway in validateStream : " + e.getLocalizedMessage());
 
      }
    }
  }

  public Set<String> getExchangeClassNames(){
    return exchangeSpecificationMap.keySet();
  }

  public Exchange getExchange(String exchangeClassName) {
    return exchangeSpecificationMap.get(exchangeClassName);
  }
}
