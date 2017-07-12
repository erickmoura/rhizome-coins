package account;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.bittrex.BittrexExamplesUtils;
import org.knowm.xchange.bittrex.v1.service.BittrexAccountServiceRaw;
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

public class BittrexAccountDemo {

  public static void main(String[] args) throws Exception {

    CertHelper.trustAllCerts();

    Exchange Bittrex = BittrexExamplesUtils.getExchange();
    AccountService accountService = Bittrex.getAccountService();

    generic(accountService);
    raw((BittrexAccountServiceRaw) accountService);
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

  private static void raw(BittrexAccountServiceRaw accountService) throws IOException {

    System.out.println("------------RAW------------");
    //System.out.println(accountService.getDepositAddress("BTC"));
    //System.out.println(accountService.getWallets());
  }

}
