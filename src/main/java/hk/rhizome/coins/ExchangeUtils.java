package hk.rhizome.coins;

import hk.rhizome.coins.marketdata.FeesMatrix;
import hk.rhizome.coins.marketdata.TradingFeePair;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class ExchangeUtils {

  private static ExchangeUtils singleton;

  private Map<String, Exchange> exchangeSpecificationMap;
  private Map<String, Integer> exchangePollingRate;

  private ExchangeUtils(){
    exchangeSpecificationMap = new HashMap<String,Exchange>();
    exchangePollingRate = new HashMap<String, Integer>();
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

      exchangeSpecificationMap.put(exchangeClassName, ExchangeFactory.INSTANCE.createExchange(spec));
    }
  }

  public void setExchanges(List<Map<String, Object>> exchanges){
    for(Map<String, Object> exchange : exchanges){
      ExchangeSpecification spec = new ExchangeSpecification((String)exchange.get("xchange_name"));
      
      spec.setApiKey((String)exchange.get("p_key"));
      spec.setSecretKey((String)exchange.get("secret"));

      FeesMatrix.setFeesMatrix(
        (String)exchange.get("exchange_name"),
        new TradingFeePair(
                new BigDecimal((Float)exchange.get("maker")),
                new BigDecimal((Float)exchange.get("taker")))
        );

      exchangeSpecificationMap.put((String)exchange.get("xchange_name"), ExchangeFactory.INSTANCE.createExchange(spec));
      exchangePollingRate.put((String)exchange.get("xchange_name"), Math.round((Float)exchange.get("polling_rate")));
    }
  }

  public Set<String> getExchangeClassNames(){
    return exchangeSpecificationMap.keySet();
  }

  public Exchange getExchange(String exchangeClassName) {
    return exchangeSpecificationMap.get(exchangeClassName);
  }

  public Integer getExchangePollingRate(String exchangeClassName){
    return exchangePollingRate.get(exchangeClassName);
  }
}
