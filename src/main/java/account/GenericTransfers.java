package account;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.poloniex.ExchangeUtils;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.poloniex.service.PoloniexAccountServiceRaw;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;
import org.knowm.xchange.utils.CertHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Bidi;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Erick de Moura
 */

public class GenericTransfers {

  protected static HashMap<String, AccountService> accountServices = new HashMap<String, AccountService>();

  protected String exchangeId;

  public GenericTransfers(Exchange exchange){

    this.exchangeId = exchange.getDefaultExchangeSpecification().getExchangeName();
    if(this.accountServices.get(exchangeId) == null) {
      this.accountServices.put(exchangeId, exchange.getAccountService());
    }
  }


  public static void main(String[] args) throws Exception {

    CertHelper.trustAllCerts();

    Exchange poloniex = ExchangeUtils.getInstance().getExchange(PoloniexExchange.class.getName());
    AccountService accountService = poloniex.getAccountService();

  }

  public String withdraw(Currency currency, BigDecimal value, String address) throws Exception {

    String result = accountServices.get(exchangeId).withdrawFunds(currency, value, address);
    System.out.println("Placed withdraw order: " + result);

    return result;
  }

  public String getDepositAddress(Currency currency) throws Exception {

    String address = accountServices.get(exchangeId).requestDepositAddress(currency);
    System.out.println("Got deposit address: " + address);

    return address;
  }

  public FundingRecord fundReceived(String transactionId) throws Exception {

    final List<FundingRecord> fundingHistory = getFundingHistory(new Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000));

    for(FundingRecord funding : fundingHistory){
      if(funding.getType() == FundingRecord.Type.DEPOSIT && funding.getExternalId() == transactionId)
        System.out.println("Funding received: " + funding);
        return funding;
    }
    return null;
  }

  public List<FundingRecord> getFundingHistory(Date startTime) throws Exception {

    final TradeHistoryParams params = accountServices.get(exchangeId).createFundingHistoryParams();
    ((TradeHistoryParamsTimeSpan)params).setStartTime(startTime);

    final List<FundingRecord> fundingHistory = accountServices.get(exchangeId).getFundingHistory(params);
    return fundingHistory;
  }
}
