package hk.rhizome.coins.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.http.HttpResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.amazonaws.util.IOUtils;

import hk.rhizome.coins.logger.AppLogger;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ESCoinsService {
    
    private final Map<String, String> elasticConfig;
    
    private static final String ES = "es";
    private static final String HOST = "host";
    private static final String REGION = "region";
    private static final String INDEX_PREFIX_CMC = "cmc-";
    private static final String RESOURCE_PATH = "/%s%s/_search";
    private static final String SEPARATOR = "-";

    public ESCoinsService(Map<String, String> configuration){
        this.elasticConfig = configuration;
    }

    public Response<String> getCoinMarketCap(Date date) throws Exception {
        Request<Void> request = signRequest(getRequest(date, INDEX_PREFIX_CMC));
        return getStringResponse(request);
    }

    private Request<Void> getRequest(Date startDate, String indexName) throws Exception {
        String index = null;
        try {
            Request<Void> request;
            String weekDate = getDateWeekFormat(startDate);
            String host = (String) elasticConfig.get(HOST);
            request = new DefaultRequest<>(ES);
            request.setEndpoint(URI.create(host));
            request.setResourcePath(String.format(RESOURCE_PATH, indexName, weekDate));
                 
            request.setHttpMethod(HttpMethodName.GET);
            return request;
        } catch (Exception e) {
            AppLogger.getLogger().error(String.format("Error creating request for index %s using date %s", index, startDate));
            throw e;
        }
    }

    private Request<Void> signRequest(Request<Void> request) {
        try {
            String regionName = (String) elasticConfig.get(REGION);
            AWS4Signer signer = new AWS4Signer();
            signer.setServiceName(ES);
            signer.setRegionName(regionName);
            AWSCredentialsProvider p = new DefaultAWSCredentialsProviderChain();
            signer.sign(request, p.getCredentials());
        } catch (Exception e) {
            AppLogger.getLogger().error("Error signing request", e);
            throw e;
        }
        return request;
    }

    private Response<String> getStringResponse(Request<?> request) {
        try {
            AmazonHttpClient client = new AmazonHttpClient(new ClientConfiguration());
            ExecutionContext executionContext = new ExecutionContext(true);
            HttpResponseHandler<AmazonClientException> handler = getErrorResponseHandler();
            HttpResponseHandler<String> responseHandler = getHttpResponseHandler();
            return client.requestExecutionBuilder().executionContext(executionContext).request(request).
                    errorResponseHandler(handler).execute(responseHandler);
        } catch (Exception e) {
            AppLogger.getLogger().error("Error processing string response", e);
            e.printStackTrace();
            throw e;
        }
    }

    private HttpResponseHandler<AmazonClientException> getErrorResponseHandler() {
        try {
            return new HttpResponseHandler<AmazonClientException>() {

                @Override
                public AmazonClientException handle(HttpResponse httpResponse) throws Exception {
                    return new AmazonClientException(String.valueOf(httpResponse.getStatusCode()));
                }

                @Override
                public boolean needsConnectionLeftOpen() {
                    return false;
                }
            };
        } catch (Exception e) {
            AppLogger.getLogger().error("Error processing error response", e);
            e.printStackTrace();
            throw e;
        }
    }

    private HttpResponseHandler<String> getHttpResponseHandler() {
        try {
            return new HttpResponseHandler<String>() {

                @Override
                public String handle(HttpResponse httpResponse) throws Exception {
                    return IOUtils.toString(httpResponse.getContent());
                }

                @Override
                public boolean needsConnectionLeftOpen() {
                    return false;
                }
            };
        } catch (Exception e) {
            AppLogger.getLogger().error("Error processing response", e);
            throw e;
        }
    }

    private String getDateWeekFormat(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return String.format("%s%s%s%s", cal.get(Calendar.YEAR), SEPARATOR, "w", week);
    }

}