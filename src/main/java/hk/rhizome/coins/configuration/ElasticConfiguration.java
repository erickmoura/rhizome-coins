package hk.rhizome.coins.configuration;

import java.util.HashMap;
import java.util.Map;

public class ElasticConfiguration {

    private String host;
    private int port;
    private String scheme;
    private Map<String, String> indicesInformation;

    public ElasticConfiguration(Map<String, Object> elasticData){
       this.host = (String)elasticData.get("host");
       this.port = (Integer)elasticData.get("port"); 
       this.scheme = (String) elasticData.get("scheme");
       this.indicesInformation = (Map) elasticData.get("indices");
    }

    public String getHost(){
        return this.host;
    }

    public int getPort(){
        return this.port;
    }

    public String getScheme(){
        return this.scheme;
    }

    public Map<String, String> getIndicesInformation(){
        return indicesInformation;
    }
}