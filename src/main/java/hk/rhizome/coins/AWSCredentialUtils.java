package hk.rhizome.coins;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import hk.rhizome.coins.logger.AppLogger;

/**
 * Provides utilities for retrieving credentials to talk to AWS
 */
public class AWSCredentialUtils {

    public static AWSCredentialsProvider getCredentialsProvider() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default] credential profile by
         * reading from the credentials file located at (~/.aws/credentials).
         */
        AWSCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = new ProfileCredentialsProvider("bot_cc_1");
        } catch (Exception e) {
        	 	AppLogger.getLogger().error("Cannot load the credentials from the credential profiles file. " +
                        "Please make sure that your credentials file is at the correct " +
                        "location (~/.aws/credentials), and is in valid format.");
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentialsProvider;
    }

}