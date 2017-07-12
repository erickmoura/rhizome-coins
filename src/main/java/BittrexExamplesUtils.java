package org.knowm.xchange.bittrex;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bittrex.v1.BittrexExchange;

/**
 * @author Zach Holmes
 */

public class BittrexExamplesUtils {

  public static Exchange getExchange() {

    ExchangeSpecification spec = new ExchangeSpecification(BittrexExchange.class);
    spec.setApiKey("your-api-key-here");
    spec.setSecretKey("your-api-key-here");

    return ExchangeFactory.INSTANCE.createExchange(spec);
  }
}
