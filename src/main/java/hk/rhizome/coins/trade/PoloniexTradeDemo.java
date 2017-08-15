package org.knowm.xchange.poloniex.trade;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.poloniex.PoloniexAdapters;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.poloniex.service.PoloniexTradeService;
import org.knowm.xchange.poloniex.service.PoloniexTradeServiceRaw;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamCurrencyPair;
import org.knowm.xchange.utils.CertHelper;

/**
 * @author Zach Holmes
 */

public class PoloniexTradeDemo {

  private static final CurrencyPair SC_BTC = new CurrencyPair("SC", "BTC");

  private static CurrencyPair currencyPair;
  private static BigDecimal buyRate;

  public static void main(String[] args) throws Exception {
    CertHelper.trustAllCerts();

    ExchangeSpecification spec = new ExchangeSpecification(PoloniexExchange.class);
    spec.setApiKey("0YLYH5CW-ZFBDX4T6-0V74ZN74-D5BW5LBV");
    spec.setSecretKey("7c565d4e144fdcf8f707ece71a68a377980ceafa6a66757121fefa2a1db8942d4a0a217263808bec0922be571de7835b39c4ba6ebbe1ae005bf642223ee26526");

    Exchange poloniex = ExchangeFactory.INSTANCE.createExchange(spec);
    TradeService tradeService = poloniex.getTradeService();
    currencyPair = SC_BTC;

    /*
     * Make sure this is below the current market rate!!
     */
    buyRate = new BigDecimal("0.00000318");

    generic(tradeService);
    raw((PoloniexTradeServiceRaw) tradeService);
  }

  private static void generic(TradeService tradeService) throws Exception {
    System.out.println("----------GENERIC----------");

    PoloniexTradeService.PoloniexTradeHistoryParams params = new PoloniexTradeService.PoloniexTradeHistoryParams();
    params.setCurrencyPair(currencyPair);
    System.out.println(tradeService.getTradeHistory(params));

    params.setStartTime(new Date());
    System.out.println(tradeService.getTradeHistory(params));

    Calendar endTime = Calendar.getInstance();
    endTime.add(Calendar.HOUR, 4);
    params.setEndTime(endTime.getTime());
    System.out.println(tradeService.getTradeHistory(params));

    LimitOrder order = new LimitOrder.Builder(OrderType.ASK, currencyPair).tradableAmount(new BigDecimal("50")).limitPrice(buyRate).build();
    String orderId = tradeService.placeLimitOrder(order);
    System.out.println("Placed order #" + orderId);

    printOpenOrders(tradeService);

    boolean canceled = tradeService.cancelOrder(orderId);
    if (canceled) {
      System.out.println("Successfully canceled order #" + orderId);
    } else {
      System.out.println("Did not successfully cancel order #" + orderId);
    }

    printOpenOrders(tradeService);
  }

  private static void raw(PoloniexTradeServiceRaw tradeService) throws IOException, InterruptedException {
    System.out.println("------------RAW------------");
    System.out.println(Arrays.asList(tradeService.returnTradeHistory(currencyPair, null, null)));
    long startTime = (new Date().getTime() / 1000) - 8 * 60 * 60;
    System.out.println(Arrays.asList(tradeService.returnTradeHistory(currencyPair, startTime, null)));
    long endTime = new Date().getTime() / 1000;
    System.out.println(Arrays.asList(tradeService.returnTradeHistory(currencyPair, startTime, endTime)));

    LimitOrder order = new LimitOrder.Builder(OrderType.BID, currencyPair).tradableAmount(new BigDecimal("1")).limitPrice(buyRate).build();
    String orderId = tradeService.buy(order).getOrderNumber().toString();
    System.out.println("Placed order #" + orderId);

    Thread.sleep(3000); // wait for order to propagate

    System.out.println(PoloniexAdapters.adaptPoloniexOpenOrders(tradeService.returnOpenOrders()));

    boolean canceled = tradeService.cancel(orderId);
    if (canceled) {
      System.out.println("Successfully canceled order #" + orderId);
    } else {
      System.out.println("Did not successfully cancel order #" + orderId);
    }

    Thread.sleep(3000); // wait for cancellation to propagate

    System.out.println(PoloniexAdapters.adaptPoloniexOpenOrders(tradeService.returnOpenOrders()));
  }

  private static void printOpenOrders(TradeService tradeService) throws Exception {
    TimeUnit.SECONDS.sleep(2);

    final OpenOrdersParamCurrencyPair params = (OpenOrdersParamCurrencyPair) tradeService.createOpenOrdersParams();
    OpenOrders openOrders = tradeService.getOpenOrders(params);
    System.out.printf("All open Orders: %s%n", openOrders);

    params.setCurrencyPair(currencyPair);
    openOrders = tradeService.getOpenOrders(params);
    System.out.printf("Open Orders for %s: %s%n: ", params, openOrders);

    params.setCurrencyPair(SC_BTC);
    openOrders = tradeService.getOpenOrders(params);
    System.out.printf("Open Orders for %s: %s%n: ", params, openOrders);
  }
}
