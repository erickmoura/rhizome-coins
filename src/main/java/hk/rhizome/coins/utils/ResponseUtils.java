package hk.rhizome.coins.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
import hk.rhizome.coins.model.UserTrades;

public class ResponseUtils {

    public static Map<String, Set<UserOrders>> getOrdersResponse(User user, Set<UserOrders> orders) {
        Map<String, Set<UserOrders>> data = new HashMap<String, Set<UserOrders>>();
        for (UserOrders order : orders) {
            String nameExchange = user.getExchanges().stream().filter(e -> e.getID() == order.getExchangeID()).collect(Collectors.toList()).get(0).getExchangeName(); 
            if (!data.containsKey(nameExchange)) {
                data.put(nameExchange, new HashSet<UserOrders>());
            }
            data.get(nameExchange).add(order);
        }
        return data;
    }

    public static Map<String, Set<UserBalances>> getBalancesResponse(User user, Set<UserBalances> balances){
        Map<String, Set<UserBalances>> data = new HashMap<String, Set<UserBalances>>();
        for (UserBalances b : balances) {
            String nameExchange = user.getExchanges().stream().filter(e -> e.getID() == b.getExchangeID()).collect(Collectors.toList()).get(0).getExchangeName(); 
            if (!data.containsKey(nameExchange)) {
                data.put(nameExchange, new HashSet<UserBalances>());
            }
            data.get(nameExchange).add(b);
        }
        return data;
    }

    public static Map<String, Set<UserTrades>> getTradesResponse(User user, Set<UserTrades> trades){
        Map<String, Set<UserTrades>> data = new HashMap<String, Set<UserTrades>>();
        for (UserTrades t : trades) {
            String nameExchange = user.getExchanges().stream().filter(e -> e.getID() == t.getExchangeID()).collect(Collectors.toList()).get(0).getExchangeName(); 
            if (!data.containsKey(nameExchange)) {
                data.put(nameExchange, new HashSet<UserTrades>());
            }
            data.get(nameExchange).add(t);
        }
        return data;
    }
}