package hk.rhizome.coins.jobs;

import hk.rhizome.coins.ExchangeUtils;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserExchanges;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class BalancesJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int exchangeId = dataMap.getIntValue("exchangeId");
        int userId = dataMap.getIntValue("userId");

        Date endDate = new Date();

        Set<UserBalances> set = new HashSet<UserBalances>();

        UserExchanges userExchanges = DbProxyUtils.getInstance().getUserExchangesProxy().getUserExchanges(userId,
                exchangeId);
        AccountService accountService = ExchangeUtils.getInstance().createXChange(userExchanges).getAccountService();

        AccountInfo accountInfo;
        try {
            accountInfo = accountService.getAccountInfo();

            for (Currency currency : accountInfo.getWallet().getBalances().keySet()) {
                Balance balance = accountInfo.getWallet().getBalances().get(currency);
                if (balance.getTotal().compareTo(BigDecimal.ZERO) != 0) {
                    UserBalances userBalances = new UserBalances(userId, exchangeId, currency.getCurrencyCode(),
                            balance.getTotal(), balance.getAvailable(), balance.getFrozen(), balance.getLoaned(),
                            balance.getBorrowed(), balance.getWithdrawing(), balance.getDepositing(), endDate);
                    set.add(userBalances);
                }
            }

        } catch (NotAvailableFromExchangeException | NotYetImplementedForExchangeException | ExchangeException
                | IOException e) {
            String message = "Error in BalanceJob for user " + userExchanges.getUser().getName() + " for exchange "
                    + userExchanges.getExchange().getExchangeName();
            AppLogger.getLogger().error(message);
            throw new JobExecutionException(message);
        }
        User user = userExchanges.getUser();
        user.setBalances(set);
        DbProxyUtils.getInstance().getUsersProxy().saveUser(user);

    }

}