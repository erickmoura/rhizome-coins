package hk.rhizome.coins;

import hk.rhizome.coins.configuration.ElasticConfiguration;
import hk.rhizome.coins.exchanges.CoinMarketCapTicker;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.logger.Log;
import hk.rhizome.coins.marketdata.ExchangeTicker;
import hk.rhizome.coins.marketdata.MarketDepth;
import hk.rhizome.coins.utils.RhizomeCoinsUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingAction;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.Header;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Elasticsearch {

    private ElasticConfiguration configuration;
    private static Elasticsearch elasticsearch;
    private static boolean initialized;

    private static final String TICKERS_INDEX = "tickers";
    private static final String MARKET_DEPTH_INDEX = "market-depth";
    private static final String CMC_INDEX = "cmc";
    private static final String LOG_INDEX = "log";
    private static final String TICKERS_TYPE = "tickers-type";
    private static final String MARKET_DEPTH_TYPE = "market-depth-type";
    private static final String CMC_TYPE = "cmc-type";
    private static final String LOG_TYPE = "log-type";


    private Elasticsearch(ElasticConfiguration configuration) {
        this.configuration = configuration;
    }

    public static Elasticsearch getElasticsearch() {
        return elasticsearch;
    }

    public static void initialize(ElasticConfiguration configuration) {
        if (elasticsearch == null) {
            elasticsearch = new Elasticsearch(configuration);
            initialized = true;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public RestHighLevelClient getHighLevelClient() throws Exception {

        if (!initialized) {
            throw new IllegalStateException("Elasticsearch client is not initialized");
        }

        RestHighLevelClient restClient = new RestHighLevelClient(RestClient
                .builder(new HttpHost(configuration.getHost(), configuration.getPort(), configuration.getScheme())));
        return restClient;
    }

    public void closeHighLevelClient(RestHighLevelClient restClient) throws IOException {
        restClient.close();
    }

    public RestClient getLowLevelClient() throws Exception {

        if (!initialized) {
            throw new IllegalStateException("Elasticsearch client is not initialized");
        }

        RestClient restClient = RestClient
                .builder(new HttpHost(configuration.getHost(), configuration.getPort(), configuration.getScheme()))
                .build();
        return restClient;
    }

    public void closeLowLevelClient(RestClient restClient) throws IOException {
        restClient.close();
    }

    public String sendTickers(ExchangeTicker ticker) {

        AppLogger.getLogger().info("Sending tickers to ES.");

        IndexRequest request = new IndexRequest(TICKERS_INDEX, TICKERS_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(ticker);
            request.source(json, XContentType.JSON);

            RestHighLevelClient highClient = getHighLevelClient();
            IndexResponse indexResponse = highClient.index(request);
            closeHighLevelClient(highClient);
            AppLogger.getLogger().info("Finishing sending tickers to ES.");
            return indexResponse.getId();

        } catch (JsonProcessingException ex) {
            AppLogger.getLogger().error("Error parsing the ticker");
        } catch (Exception ex) {
            AppLogger.getLogger().error(ex);
        }
        return "";

    }

    public String sendMarketDepth(MarketDepth marketDepth) {

        AppLogger.getLogger().info("Sending market depth to ES");

        IndexRequest request = new IndexRequest(MARKET_DEPTH_INDEX, MARKET_DEPTH_TYPE);
        ObjectMapper mapper = new ObjectMapper();
        try {

            String json = mapper.writeValueAsString(marketDepth);
            request.source(json, XContentType.JSON);

            RestHighLevelClient highClient = getHighLevelClient();
            IndexResponse indexResponse = highClient.index(request);
            closeHighLevelClient(highClient);

            AppLogger.getLogger().info("Finished sending market depth.");

            return indexResponse.getId();
        } catch (JsonProcessingException ex) {
            AppLogger.getLogger().error("Error parsing the market depth");
        } catch (Exception ex) {
            AppLogger.getLogger().error(ex);
        }
        return "";

    }

    public String sendCoinMarketCap(List<CoinMarketCapTicker> tickerList) {

        AppLogger.getLogger().info("Sending coin market cap to ES");

        ObjectMapper mapper = new ObjectMapper();
        
        String response = "";
        try {
            RestHighLevelClient highClient = getHighLevelClient();
        
            for (CoinMarketCapTicker c : tickerList) {
                
                IndexRequest request = new IndexRequest(CMC_INDEX, CMC_TYPE);
                String json = mapper.writeValueAsString(c);
                request.source(json, XContentType.JSON);

                IndexResponse indexResponse = highClient.index(request);
                response = indexResponse.getId();
            }
            closeHighLevelClient(highClient);

            AppLogger.getLogger().info("Finished sending coin market cap.");

            return response;
        } catch (JsonProcessingException ex) {
            AppLogger.getLogger().error("Error parsing the coin market cap");
        } catch (Exception ex) {
            AppLogger.getLogger().error(ex);
        }
        return response;
    }

    public String sendLog(Log log){
        
        ObjectMapper mapper = new ObjectMapper();
        
        String response = "";
        try {
            RestHighLevelClient highClient = getHighLevelClient();
            IndexRequest request = new IndexRequest(LOG_INDEX, LOG_TYPE);
            String json = mapper.writeValueAsString(log);
            request.source(json, XContentType.JSON);

            IndexResponse indexResponse = highClient.index(request);
            response = indexResponse.getId();

            closeHighLevelClient(highClient);
        
            return response;
        } catch (JsonProcessingException ex) {
            
        } catch (Exception ex) {
            
        }
        return response;
    }

    public void initCluster() throws Exception {
        System.out.println("ES-Creating indices");
        RestHighLevelClient highClient = getHighLevelClient();
        for (String indexName : configuration.getIndicesInformation().keySet()) {
            try {
                createIndex(highClient, indexName, String.format("%s-type", indexName),
                        configuration.getIndicesInformation().get(indexName));
            } catch (ElasticsearchStatusException ex) {
                if (ex.getMessage().contains("resource_already_exists_exception"))
                    System.out.println(String.format("Index %s already exists. Ignoring the error...", indexName));
                else
                    throw new RuntimeException(ex.getMessage());
            }

        }
        System.out.println("ES-Indices created");
        closeHighLevelClient(highClient);
    }

    private void createIndex(RestHighLevelClient client, String indexName, String indexType, String pathTemplate)
            throws Exception {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 1));
        String template = RhizomeCoinsUtil.getResourceFile(pathTemplate);
        request.mapping(String.format("%s-type", indexName), template, XContentType.JSON);

        CreateIndexResponse createIndexResponse = client.indices().create(request);
        if (!createIndexResponse.isAcknowledged())
            AppLogger.getLogger().warn(String.format("Index %s not created.", indexName));
        else
            AppLogger.getLogger().info(String.format("Index %s is created.", indexName));
    }

}
