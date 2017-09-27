package hk.rhizome.coins;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import hk.rhizome.coins.account.ExchangeBalance;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.trade.UserTrade;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by erickmoura on 2/7/2017.
 * Continuously sends simulated stock trades to Kinesis
 */
public class KinesisGateway {

    private static final String KINESIS_TICKER_STREAM = "coins-firehose-tickers";
    private static final String KINESIS_MARKET_DEPTH_STREAM = "coins-firehose-market-depth";
    private static final String KINESIS_ORDERS_STREAM = "coins-firehose-orders";
    private static final String KINESIS_USER_TRADES_STREAM = "coins-firehose-user-trades";
    private static final String KINESIS_BALANCES_STREAM = "coins-balances";

    private static final String KINESIS_DEFAULT_REGION = "us-east-1";
    private static final Log LOG = LogFactory.getLog(KinesisGateway.class);

    private AmazonKinesisFirehose kinesisClient;
    private String sequenceNumberOfPreviousRecord = "0";

    private final static ObjectMapper JSON = new ObjectMapper();

    private void validateStream(AmazonKinesisFirehose kinesisClient, String streamName){

        DescribeDeliveryStreamRequest describeHoseRequest = new DescribeDeliveryStreamRequest()
                .withDeliveryStreamName(streamName);
        DescribeDeliveryStreamResult  describeHoseResult = null;
        String status = "UNDEFINED";
        try {
            describeHoseResult = kinesisClient.describeDeliveryStream(describeHoseRequest);
            status = describeHoseResult.getDeliveryStreamDescription().getDeliveryStreamStatus();
        } catch (Exception e) {
        		AppLogger.getLogger().error("Exception in KinesisGateway in validateStream : " + e.getLocalizedMessage());
        }
        if(status.equalsIgnoreCase("ACTIVE")){
            //return;
        }
        else if(status.equalsIgnoreCase("CREATING")){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            		AppLogger.getLogger().error("Exception in KinesisGateway in validateStream : " + e.getLocalizedMessage());
            }
        }
        else {
            AppLogger.getLogger().info("Status = " + status);
        }
    }

    public static void main(String[] args) throws Exception {

        KinesisGateway g = new KinesisGateway();
        g.validateStream();
        g.sendTicker(null);
    }

    public byte[] toJsonAsBytes(Object o) {
        try {
            return JSON.writeValueAsBytes(o);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Uses the Kinesis client to send the stock hk.rhizome.coins.trade to the given stream.
     *
     * @param ticker instance representing the ticker
     * */
    public PutRecordResult sendTicker(ExchangeTicker ticker) throws Exception {

        if (null==kinesisClient) {
            AppLogger.getLogger().error("Error in KinesisGateway in sendTicker : Kinesis Client not initialized.");
            return null;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(ticker)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_TICKER_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
        return res;
    }

    public PutRecordResult sendMarketDepth(MarketDepth marketDepth) {
        if (null==kinesisClient) {
        		AppLogger.getLogger().error("Error in KinesisGateway in sendMarketDepth : Kinesis Client not initialized.");
            return null;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(marketDepth)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_MARKET_DEPTH_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
        return res;
    }

    public PutRecordResult sendOrder(Order openOrder) {
        if (null==kinesisClient) {
        		AppLogger.getLogger().error("Error in KinesisGateway in sendOrder : Kinesis Client not initialized.");
            return null;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(openOrder)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_ORDERS_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
        return res;
    }

    public PutRecordResult sendUserTrade(UserTrade trade) {
        if (null==kinesisClient) {
        		AppLogger.getLogger().error("Error in KinesisGateway in sendUserTrade : Kinesis Client not initialized.");
            return null;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(trade)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_USER_TRADES_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
        return res;
    }

    public PutRecordResult sendBalance(ExchangeBalance balance) {
        if (null==kinesisClient) {
            System.err.println("Kinesis Client not initialized.");
            return null;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(balance)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_BALANCES_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
        return res;
    }

    public void validateStream() throws Exception {
        String streamName = KINESIS_TICKER_STREAM;
        String regionName = KINESIS_DEFAULT_REGION;
        Region region = RegionUtils.getRegion(regionName);
        if (region == null) {
        		AppLogger.getLogger().error("Error in KinesisGateway in validateStream : " + regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        AWSCredentials credentials = AWSCredentialUtils.getCredentialsProvider().getCredentials();

        kinesisClient = new AmazonKinesisFirehoseClient(credentials,
                KinesisConfiguration.getClientConfigWithUserAgent());
        kinesisClient.setRegion(region);

        // Validate that the stream exists and is active
        validateStream(kinesisClient, streamName);
    }

}
