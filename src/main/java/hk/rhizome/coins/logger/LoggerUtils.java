package hk.rhizome.coins.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClient;
import com.amazonaws.services.kinesisfirehose.model.DescribeDeliveryStreamRequest;
import com.amazonaws.services.kinesisfirehose.model.DescribeDeliveryStreamResult;
import com.amazonaws.services.kinesisfirehose.model.PutRecordRequest;
import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;
import com.amazonaws.services.kinesisfirehose.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;

import hk.rhizome.coins.AWSCredentialUtils;
import hk.rhizome.coins.KinesisConfiguration;
import hk.rhizome.coins.LoggerConfiguration;

import org.json.simple.JSONObject;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;



public class LoggerUtils{
    
    private final static String KINESIS_LOGGER_STREAM = "coins-firehose-log";
    private static final String KINESIS_DEFAULT_REGION = "us-east-1";
    
    private final static String SPACE = " ";

    private static LoggerUtils logger;
    private static LoggerFormatter formatter;

    private static Object lock = new Object();
    
    private Level level = Level.OFF;
    private static boolean initialized;
    
    private final static ObjectMapper JSON = new ObjectMapper();    
    private AmazonKinesisFirehose kinesisClient;

    public enum Level {
        OFF,
        ERROR,
        WARN,
        INFO,
        DEBUG
    }

    /**
     * Get the logger.
     *
     * @return the logger
     */
    public static LoggerUtils getLogger() {
        synchronized (lock) {
            if (logger == null) {
                logger = new LoggerUtils();
            }
        }

        return logger;
    }
    
    public static LoggerConfiguration getLoggerConfiguration(Map<String, String> configuration) {
    		LoggerConfiguration l = new LoggerConfiguration();
    		l.setLogLevel((String)configuration.get("level"));
   		return l;
    }

    /**
     * Set the level of the log.
     *
     * @param formatter      The log formatter.
     * @param level          The level
     * @return true if success, false otherwise
     * @throws RuntimeException when it finds a problem with the name or number of files
     */
    public synchronized boolean initialize(LoggerFormatter loggerFormatter,
                                           Level level)  {

        if (!initialized) {

            this.level = level;
            formatter = loggerFormatter;
            
            String streamName = KINESIS_LOGGER_STREAM;
            String regionName = KINESIS_DEFAULT_REGION;
            Region region = RegionUtils.getRegion(regionName);
            if (region == null) {
                System.err.println(regionName + " is not a valid AWS region.");
                throw new RuntimeException("Region " + regionName + "is not a valid AWS region.");
            }
            try {
            AWSCredentials credentials = AWSCredentialUtils.getCredentialsProvider().getCredentials();

            kinesisClient = new AmazonKinesisFirehoseClient(credentials,
                    KinesisConfiguration.getClientConfigWithUserAgent());
            kinesisClient.setRegion(region);

            // Validate that the stream exists and is active
            validateStream(kinesisClient, streamName);

            initialized = true;
            debug("Log initialized.");
            } catch(Exception ex) {
            		error("Error getting the AWS credentials");
            }
        } else {
        		error("Log not initialized");
        }
        return initialized;
    }
    
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
            AppLogger.getLogger().info("Stream with status ACTIVE");
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

    /**
     * If the logger is initialized
     *
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Log a message.
     * LogMessage must be initialized.
     *
     * @param message  The message.
     * @param logLevel The log level
     */
    private void log(Object message, Level logLevel) {
        write(message, logLevel);
    }

    /**
     * Log a throwable.
     * LogMessage must be initialized.
     *
     * @param throwable The throwable.
     * @param logLevel  The log level
     */
    private void log(Throwable throwable, Level logLevel) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        write(pw.toString(), logLevel);
    }

    /**
     * Log a message and athrowable.
     * LogMessage must be initialized.
     *
     * @param message   The message.
     * @param throwable The throwable.
     * @param logLevel  The log level
     */
    private void log(Object message, Throwable throwable, Level logLevel) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        write(message + SPACE + sw.toString(), logLevel);
    }

    /**
     * Log a ERROR message.
     * LogMessage must be initialized.
     *
     * @param message The message.
     */
    public void error(Object message) {
        log(message, Level.ERROR);
    }

    /**
     * Log a ERROR message and a throwable.
     * LogMessage must be initialized.
     *
     * @param message   The message.
     * @param throwable The throwable.
     */
    public void error(Object message, Throwable throwable) {
        log(message, throwable, Level.ERROR);

    }

    /**
     * Log a WARN message.
     * LogMessage must be initialized.
     *
     * @param message The message.
     */
    public void warn(Object message) {
        log(message, Level.WARN);
    }

    /**
     * Log a WARN message and a throwable.
     * LogMessage must be initialized.
     *
     * @param message   The message.
     * @param throwable The throwable.
     */
    public void warn(Object message, Throwable throwable) {
        log(message, throwable, Level.WARN);
    }

    /**
     * Log a DEBUG message.
     * LogMessage must be initialized.
     *
     * @param message The message.
     */
    public void debug(Object message) {
        log(message, Level.DEBUG);
    }

    /**
     * Log a DEBUG message and a throwable.
     * LogMessage must be initialized.
     *
     * @param message   The message.
     * @param throwable The throwable.
     */
    public void debug(Object message, Throwable throwable) {
        log(message, throwable, Level.DEBUG);
    }

    /**
     * Log a INFO message.
     * LogMessage must be initialized.
     *
     * @param message The message.
     */
    public void info(Object message) {
        log(message, Level.INFO);
    }

    /**
     * Log a INFO throwable.
     * LogMessage must be initialized.
     *
     * @param throwable The throwable.
     */
    public void info(Throwable throwable) {
        log(throwable, Level.INFO);
    }

    /**
     * Log a INFO message and a throwable.
     * LogMessage must be initialized.
     *
     * @param message   The message.
     * @param throwable The throwable.
     */
    public void info(Object message, Throwable throwable) {
        log(message, throwable, Level.INFO);
    }

    /**
     * Method used to write the log to a file.
     *
     * @param message The message.
     * @param level   The level of message.
     */
    private void write(Object message, Level level) {
        if (!initialized) {
            throw new RuntimeException("Not initialized!!");
        }

        if (level.ordinal() <= this.level.ordinal()) {
            if (formatter == null) {
                saveToStream(level.toString(), message.toString());
                return;
            }

            if (message instanceof Map) {
                String msg = formatter.formatLogMap((Map) message, level);
                saveToStream(level.toString(), msg);
                return;
            }

            String msg = formatter.formatLogMsg(message.toString(), level);
            saveToStream(level.toString(), msg);
        }
    }

    /**
     * Method used to save the a string to a file.
     *
     * @param level    The level
     * @param value    The string.
     */
    private void saveToStream(final String level, final String value) {
            try {
                
                //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                
                JSONObject json = new JSONObject();
                json.put("message", value);
                json.put("level", level);
                json.put("timestamp", date);
                
            		Record record = new Record()
                        .withData(ByteBuffer.wrap(toJsonAsBytes(json)));
                PutRecordRequest putRecordInHoseRequest = new PutRecordRequest()
                        .withDeliveryStreamName(KINESIS_LOGGER_STREAM)
                        .withRecord(record);

                PutRecordResult res = kinesisClient.putRecord(putRecordInHoseRequest);
                
            } catch (Exception ioe) {
                System.err.println(ioe);
            }
    }
    
    public byte[] toJsonAsBytes(Object o) {
        try {
            return JSON.writeValueAsBytes(o);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Returns the current level
     *
     * @return the current level
     */
    public Level level() {
        return level;
    }

    /**
     * Returns if the level is enabled
     *
     * @return if the level is enabled
     */
    public boolean levelEnabled(LoggerUtils.Level level) {
        return level().ordinal() >= level.ordinal();
    }
}
