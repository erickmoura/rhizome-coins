package hk.rhizome.coins.utils;

import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseUtils {

    public static Map<Integer, Exchanges> getMapExchanges(List<Exchanges> list) {
        Map<Integer, Exchanges> map = new HashMap<Integer, Exchanges>();
        for (Exchanges exchange : list) {
            if (!map.containsKey(exchange.getID())) {
                map.put(exchange.getID(), exchange);
            }
        }
        return map;
    }

    //TODO: if userOrder has the exchange_id, why this method needs to receive mapExchanges?
    public static Map<String, List<UserOrders>> getOrdersResponse(List<UserOrders> orders, Map<Integer, Exchanges> mapExchanges) {
        Map<String, List<UserOrders>> data = new HashMap<String, List<UserOrders>>();
        for (UserOrders order : orders) {
            String nameExchange = mapExchanges.get(order.getExchangeID()).getExchangeName();
            if (!data.containsKey(nameExchange)) {
                data.put(nameExchange, new ArrayList<UserOrders>());
            }
            data.get(nameExchange).add(order);
        }
        return data;
    }

    //TODO: if userOrder has the exchange_id, why this method needs to receive mapExchanges?
    public static Map<String, List<UserBalances>> getBalancesResponse(List<UserBalances> balances, Map<Integer, Exchanges> mapExchanges) {
        Map<String, List<UserBalances>> data = new HashMap<String, List<UserBalances>>();
        for (UserBalances b : balances) {
            String nameExchange = mapExchanges.get(b.getExchangeID()).getExchangeName();
            if (!data.containsKey(nameExchange)) {
                data.put(nameExchange, new ArrayList<UserBalances>());
            }
            data.get(nameExchange).add(b);
        }
        return data;
    }

}