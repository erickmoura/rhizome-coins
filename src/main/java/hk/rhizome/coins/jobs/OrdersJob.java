package hk.rhizome.coins.jobs;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.model.Coins;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserOrders;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class OrdersJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int exchangeId = dataMap.getIntValue("exchangeId");
        int userId = dataMap.getIntValue("userId");

        Set<UserOrders> set = new HashSet<UserOrders>();

        UserExchanges userExchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getUserExchanges(userId,
                exchangeId);

        CoinsSetService coinsService = CoinsSetService.getInstance();
        List<Coins> coins = coinsService.getCoins();

        ExchangeSpecification exSpec = new ExchangeSpecification(userExchanges.getExchange().getXchangeName());
        exSpec.setApiKey(userExchanges.getKey());
        exSpec.setSecretKey(userExchanges.getSecret());
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exSpec);

        MarketDataService service = ExchangeUtils.getInstance().createXChange(userExchanges).getMarketDataService();

        try {

            List<CurrencyPair> currencyPairs = exchange.getExchangeSymbols();
            for (CurrencyPair cp : currencyPairs) {
                if (filterCoin(coins, cp.base.getDisplayName()) && filterCoin(coins, cp.counter.getDisplayName())) {
                    OrderBook orderBook = service.getOrderBook(cp);

                    for (LimitOrder order : orderBook.getAsks()) {
                        if (order.getId() != null) {
                            UserOrders o = new UserOrders(order.getId(), userExchanges.getUser().getID(),
                                    userExchanges.getExchange().getID(), order.getCurrencyPair().toString(),
                                    order.getType().toString(), order.getStatus().toString(), order.getTradableAmount(),
                                    order.getCumulativeAmount(), order.getAveragePrice(), order.getTimestamp());
                            set.add(o);
                        }
                    }
                    for (LimitOrder order : orderBook.getBids()) {
                        if (order.getId() != null) {
                            UserOrders o = new UserOrders(order.getId(), userExchanges.getUser().getID(),
                                    userExchanges.getExchange().getID(), order.getCurrencyPair().toString(),
                                    order.getType().toString(), order.getStatus().toString(), order.getTradableAmount(),
                                    order.getCumulativeAmount(), order.getAveragePrice(), order.getTimestamp());
                            set.add(o);
                        }
                    }
                }
            }
        } catch (NotAvailableFromExchangeException | NotYetImplementedForExchangeException | ExchangeException
                | IOException e) {
            String message = "Error executing the OrdersJOb for user " + userExchanges.getUser().getName()
                    + " and exchange " + userExchanges.getExchange().getExchangeName();
            AppLogger.getLogger().error(message);
            throw new JobExecutionException(message);
        }
        User user = userExchanges.getUser();
        user.setOrders(set);
        DbProxyUtils.getInstance().getUsersProxy().saveUser(user);

    }

    private boolean filterCoin(List<Coins> coins, String name) {
        for (Coins c : coins) {
            if (c.getName().equals(name))
                return true;
        }
        return false;
    }

}