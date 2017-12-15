package hk.rhizome.coins.jobs;

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.CoinsSetService;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.marketdata.PricingsMatrix;
import hk.rhizome.coins.model.Coins;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.utils.CertHelper;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class XChangeJob extends RhizomeJob {

    protected static HashMap<String, MarketDataService> dataServices = new HashMap<String, MarketDataService>();
   
    public XChangeJob() {
        try {
            CertHelper.trustAllCerts();
        } catch (Exception e) {
            AppLogger.getLogger().error("Error in XChangeJob in XChangeJob : " + e.getLocalizedMessage());
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String exchangeId = dataMap.getString("exchangeID");
            String currency = dataMap.getString("currencyPair");
            System.out.println(currency);
            System.out.println(exchangeId);
            CurrencyPair currencyPair = new CurrencyPair(currency.split("/")[0], currency.split("/")[1]);
            ExchangeSpecification exSpec = new ExchangeSpecification(exchangeId);
            exSpec.setApiKey("key");
            exSpec.setSecretKey("secret");
            Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exSpec);
            
            AppLogger.getLogger().info("Start job in XChangeJob for exchange " + exchangeId + " with currency " + currencyPair);

            if (dataServices.get(exchangeId) == null) {
                dataServices.put(exchangeId, exchange.getMarketDataService());
            }
            
            // Collect Ticker data
            ExchangeTicker ticker = new ExchangeTicker(exchangeId,
                    dataServices.get(exchangeId).getTicker(currencyPair));
            AppLogger.getLogger().info(ticker);
            kinesisGateway.sendTicker(ticker);

            // Insert ticker into the pricings matrix
            PricingsMatrix.setTicker(exchangeId, currencyPair, ticker);

            // Collect Order Book data
            Date timestamp = ticker.getTimestamp();
            OrderBook orderBook = dataServices.get(exchangeId).getOrderBook(currencyPair);

            MarketDepth marketDepth = new MarketDepth(timestamp, orderBook);
            marketDepth.setExchange(exchangeId);
            AppLogger.getLogger().info(marketDepth);
            kinesisGateway.sendMarketDepth(marketDepth);

            AppLogger.getLogger()
                    .info("End job in XChangeJob for exchange " + exchangeId + " with currency " + currencyPair);

        } catch (Exception e) {
            AppLogger.getLogger().error("Error in XChangeJob in execute.Failed to poll ");
            e.printStackTrace();
            throw (new JobExecutionException("Error in XChangeJob : " + e.getLocalizedMessage()));
        }

    }

}
