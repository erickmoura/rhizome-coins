package hk.rhizome.coins.jobs;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CurrencySetService;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.UserTrades;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UserTradesJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int exchangeId = dataMap.getIntValue("exchangeId");
        int userId = dataMap.getIntValue("userId");

        // Go initially to 1000 days back in time
        long startTime = (new Date().getTime() / 1000) - 24 * 60 * 60 * 1000;
        Date lastPolledDate = new Date(startTime * 1000);

        TradeHistoryParamsAll params = new TradeHistoryParamsAll();

        params.setCurrencyPairs(CurrencySetService.getCurrencySet());
        params.setEndTime(new Date());
        params.setStartTime(lastPolledDate);

        UserExchanges userExchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getUserExchanges(userId,
                exchangeId);
        TradeService tradeService = ExchangeUtils.getInstance().createXChange(userExchanges).getTradeService();
        Set<UserTrades> set = new HashSet<UserTrades>();

        try {
            for (UserTrade trade : tradeService.getTradeHistory(params).getUserTrades()) {
                if(trade.getFeeAmount() != null){
                    UserTrades t = new UserTrades(userId, exchangeId, trade.getId(), trade.getOrderId(),
                            trade.getCurrencyPair().toString(), trade.getFeeAmount(),
                            trade.getFeeCurrency().getDisplayName(), trade.getTradableAmount(), trade.getPrice(),
                            trade.getTimestamp(), trade.getType().toString());
                    set.add(t);
                }
            }
        } catch (IOException e) {
            String message = "Error executing the UserTradesJob for user " + userExchanges.getUser().getName()
                    + " and exchange " + userExchanges.getExchange().getExchangeName();
            AppLogger.getLogger().error(message);
            throw new JobExecutionException(message);
        }

        User user = userExchanges.getUser();
        user.setTrades(set);
        DbProxyUtils.getInstance().getUsersProxy().saveUser(user);

    }

}