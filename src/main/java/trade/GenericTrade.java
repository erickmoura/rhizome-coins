package trade;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.poloniex.service.PoloniexTradeService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamCurrencyPair;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Erick de Moura
 */

public class GenericTrade {

  protected static HashMap<String, TradeService> tradeServices = new HashMap<String, TradeService>();

  private static final CurrencyPair REP_ETH = new CurrencyPair("REP", "ETH");

  private static CurrencyPair currencyPair;
  private static BigDecimal buyRate;
  private static BigDecimal sellRate;

  private static void buy() throws Exception {

    /*
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

    LimitOrder order = new LimitOrder.Builder(OrderType.BID, currencyPair).tradableAmount(new BigDecimal(".1")).limitPrice(xmrBuyRate).build();
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
    */
  }


  private static void printOpenOrders(TradeService tradeService) throws Exception {
    /*
    TimeUnit.SECONDS.sleep(2);

    final OpenOrdersParamCurrencyPair params = (OpenOrdersParamCurrencyPair) tradeService.createOpenOrdersParams();
    OpenOrders openOrders = tradeService.getOpenOrders(params);
    System.out.printf("All open Orders: %s%n", openOrders);

    params.setCurrencyPair(currencyPair);
    openOrders = tradeService.getOpenOrders(params);
    System.out.printf("Open Orders for %s: %s%n: ", params, openOrders);

    params.setCurrencyPair(REP_ETH);
    openOrders = tradeService.getOpenOrders(params);
    System.out.printf("Open Orders for %s: %s%n: ", params, openOrders);
    */
  }
}
