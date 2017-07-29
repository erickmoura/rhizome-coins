package org.knowm.xchange.poloniex;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

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

      exchangeSpecificationMap.put(exchangeClassName, ExchangeFactory.INSTANCE.createExchange(spec));
    }
  }

  public Set<String> getExchangeClassNames(){
    return exchangeSpecificationMap.keySet();
  }

  public Exchange getExchange(String exchangeClassName) {
    return exchangeSpecificationMap.get(exchangeClassName);
  }
}
