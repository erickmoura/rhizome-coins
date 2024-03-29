package hk.rhizome.coins.account;

import hk.rhizome.coins.ExchangeUtils;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.poloniex.service.PoloniexAccountServiceRaw;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.utils.CertHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Zach Holmes
 */

public class PoloniexAccountDemo {

  public static void main(String[] args) throws Exception {

    CertHelper.trustAllCerts();

    ExchangeSpecification spec = new ExchangeSpecification(PoloniexExchange.class);
    spec.setApiKey("0YLYH5CW-ZFBDX4T6-0V74ZN74-D5BW5LBV");
    spec.setSecretKey("7c565d4e144fdcf8f707ece71a68a377980ceafa6a66757121fefa2a1db8942d4a0a217263808bec0922be571de7835b39c4ba6ebbe1ae005bf642223ee26526");

    Exchange poloniex = ExchangeFactory.INSTANCE.createExchange(spec);
    AccountService accountService = poloniex.getAccountService();

    generic(accountService);
    raw((PoloniexAccountServiceRaw) accountService);
  }

  private static void generic(AccountService accountService) throws IOException {

    System.out.println("----------GENERIC----------");
    System.out.println(accountService.requestDepositAddress(Currency.BTC));
    System.out.println(accountService.getAccountInfo());

    System.out.println(accountService.withdrawFunds(Currency.BTC, new BigDecimal("0.03"), "13ArNKUYZ4AmXP4EUzSHMAUsvgGok74jWu"));

    final TradeHistoryParams params = accountService.createFundingHistoryParams();
    ((TradeHistoryParamsTimeSpan)params).setStartTime(new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));

    final List<FundingRecord> fundingHistory = accountService.getFundingHistory(params);
    for (FundingRecord fundingRecord : fundingHistory) {
      System.out.println(fundingRecord);
    }
  }

  private static void raw(PoloniexAccountServiceRaw accountService) throws IOException {

    System.out.println("------------RAW------------");
    System.out.println(accountService.getDepositAddress("BTC"));
    System.out.println(accountService.getWallets());
  }

}
