package marketdata;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.KinesisGateway;
import org.knowm.xchange.bittrex.v1.BittrexExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.CertHelper;

/**
 * Created by erickmoura on 8/7/2017.
 */
public class MarketDataPoller {

    protected static Exchange exchange;
    protected static KinesisGateway kinesisGateway;
    protected static MarketDataService dataService;

    protected CurrencyPair currencyPair;

    protected static void init(String exchangeClassName){

        kinesisGateway = new KinesisGateway();
        try {
            kinesisGateway.validateStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CertHelper.trustAllCerts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        exchange = ExchangeFactory.INSTANCE.createExchange(exchangeClassName);
        dataService = exchange.getMarketDataService();
    }
}
