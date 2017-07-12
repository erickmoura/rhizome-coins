/**
 * Created by erickmoura on 2/7/2017.
 */
package org.knowm.xchange;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import marketdata.MarketDepth;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.io.IOException;
import java.nio.ByteBuffer;
//import com.amazonaws.services.kinesis.samples.stocktrades.model.StockTrade;
//import com.amazonaws.services.kinesis.samples.stocktrades.utils.ConfigurationUtils;
//import com.amazonaws.services.kinesis.samples.stocktrades.utils.CredentialUtils;

/**
 * Continuously sends simulated stock trades to Kinesis
 */
public class KinesisGateway {

    private static final String KINESIS_TICKER_STREAM = "coins-firehose-tickers";
    private static final String KINESIS_MARKET_DEPTH_STREAM = "coins-firehose-market-depth";

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
            System.out.println(e.getLocalizedMessage());
            //checkHoseStatus();
        }
        if(status.equalsIgnoreCase("ACTIVE")){
            //return;
        }
        else if(status.equalsIgnoreCase("CREATING")){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //checkHoseStatus();
        }
        else {
            System.out.println("Status = " + status);
        }
    }

    /*
    private static void validateStream(AmazonKinesisFirehose kinesisClient, String streamName) {
        try {
            DescribeDeliveryStreamResult result = kinesisClient.describeStream(streamName);
            if (!"ACTIVE".equals(result.getStreamDescription().getStreamStatus())) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        } catch (ResourceNotFoundException e) {
            System.err.println("Stream " + streamName + " does not exist. Please create it in the console.");
            System.err.println(e);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }
    */

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
     * Uses the Kinesis client to send the stock trade to the given stream.
     *
     * @param ticker instance representing the ticker
     * */
    public void sendTicker(Ticker ticker) throws Exception {

        if (null==kinesisClient) {
            System.err.println("Kinesis Client not initialized.");
            return;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(ticker)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_TICKER_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);

        //putRecordRequest.setData(  ByteBuffer.wrap( String.format( "testData-%d", 0 ).getBytes() ));
        //putRecordRequest.setData(ByteBuffer.wrap(toJsonAsBytes(ticker)));
        //putRecordRequest.setPartitionKey( String.format( "partitionKey-%d", 0 ));
        //putRecordRequest.setSequenceNumberForOrdering( sequenceNumberOfPreviousRecord );
        //PutRecordResult putRecordResult = kinesisClient.putRecord( putRecordRequest );
        //sequenceNumberOfPreviousRecord = putRecordResult.getSequenceNumber();
    }

    public void sendMarketDepth(MarketDepth marketDepth) {
        if (null==kinesisClient) {
            System.err.println("Kinesis Client not initialized.");
            return;
        }

        Record record = new Record()
                .withData(ByteBuffer.wrap(toJsonAsBytes(marketDepth)));
        PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                .withDeliveryStreamName(KINESIS_MARKET_DEPTH_STREAM)
                .withRecord(record);

        PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);

    }

    public void validateStream() throws Exception {
        String streamName = KINESIS_TICKER_STREAM;
        String regionName = KINESIS_DEFAULT_REGION;
        Region region = RegionUtils.getRegion(regionName);
        if (region == null) {
            System.err.println(regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        AWSCredentials credentials = org.knowm.xchange.CredentialUtils.getCredentialsProvider().getCredentials();

        kinesisClient = new AmazonKinesisFirehoseClient(credentials,
                org.knowm.xchange.ConfigurationUtils.getClientConfigWithUserAgent());
        kinesisClient.setRegion(region);

        // Validate that the stream exists and is active
        validateStream(kinesisClient, streamName);
    }



}
