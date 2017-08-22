package hk.rhizome.coins.trade;

import hk.rhizome.coins.KinesisGateway;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.poloniex.service.PoloniexTradeService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParamCurrencyPair;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Erick de Moura
 */

public class GenericTrade {

  protected static HashMap<String, TradeService> tradeServices = new HashMap<String, TradeService>();

  protected String exchangeId;
  protected CurrencyPair currencyPair;

  protected static KinesisGateway kinesisGateway = new KinesisGateway();;


  public GenericTrade(Exchange exchange, CurrencyPair currencyPair){

    this.exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
    this.currencyPair = currencyPair;

    if(this.tradeServices.get(exchangeId) == null) {
      this.tradeServices.put(exchangeId, exchange.getTradeService());
    }
  }

  public String buy(BigDecimal amount, BigDecimal limitPrice) throws Exception {

    LimitOrder order = new LimitOrder.Builder(OrderType.BID, currencyPair).tradableAmount(amount).limitPrice(limitPrice).build();
    String orderId = tradeServices.get(exchangeId).placeLimitOrder(order);

    //Feed index
    kinesisGateway.sendOrder(order);

    System.out.println("Placed buy order #" + orderId);

    return orderId;
  }

  public String sell(BigDecimal amount, BigDecimal limitPrice) throws Exception {

    LimitOrder order = new LimitOrder.Builder(OrderType.ASK, currencyPair).tradableAmount(amount).limitPrice(limitPrice).build();
    String orderId = tradeServices.get(exchangeId).placeLimitOrder(order);

    //Feed index
    kinesisGateway.sendOrder(order);

    System.out.println("Placed sell order #" + orderId);
    return orderId;
  }

  /*
  public Boolean orderExecuted(String orderId) throws Exception {

    OpenOrders openOrders = openOrders();

    for(Order o : openOrders.getOpenOrders()){
      if(o.getId() == orderId)
        return false;
    }
    return true;
  }
  */

  public Boolean orderFilled(Order order) throws Exception {

    Boolean filled = order.getStatus() == Order.OrderStatus.FILLED;
    return filled;
  }

  public OpenOrders openOrders() throws Exception {

    final OpenOrdersParamCurrencyPair params = (OpenOrdersParamCurrencyPair) tradeServices.get(exchangeId).createOpenOrdersParams();
    OpenOrders openOrders = tradeServices.get(exchangeId).getOpenOrders(params);

    System.out.printf("%s: All open Orders: %s%n", exchangeId, openOrders);

    return openOrders;
  }

  /*
  public UserTrades tradeHistory(Date startTime) throws Exception {

    PoloniexTradeService.PoloniexTradeHistoryParams params = new PoloniexTradeService.PoloniexTradeHistoryParams();

    if(startTime != null)
      params.setStartTime(new Date());
    params.setCurrencyPair(currencyPair);

    UserTrades userTrades = tradeServices.get(exchangeId).getTradeHistory(params);

    for(UserTrade trade : userTrades.getUserTrades()){
      //Feed index
      kinesisGateway.sendUserTrade(trade);
    }
    return userTrades;
  }
  */

}
