package hk.rhizome.coins; 
/**
 * Created by erickmoura on 28/7/2017.
 *
 */

import java.util.Collections;
import java.util.Map;

import io.dropwizard.Configuration; 
import javax.validation.constraints.NotNull; 

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;


public class RhizomeCoinsConfiguration extends Configuration {
    
    @NotNull
    private Map<String, Map<String, String>> exchanges = Collections.emptyMap();

    @NotNull
    private Map<String, String> logging = Collections.emptyMap();
    
    @NotNull
    private Map<String, String> database = Collections.emptyMap();

    @NotNull
    private Map<String, Object> elastic = Collections.emptyMap();


    @JsonProperty("exchanges")
    public Map<String, Map<String, String>> getExchanges() {
        return exchanges;
    }

    @JsonProperty("elastic")
    public Map<String, Object> getElastic(){
        return elastic;
    }

    @JsonProperty("elastic")
    public void setElastic(Map<String, Object> elastic){
        this.elastic = elastic;
    }

    @JsonProperty("exchanges")
    public void setExchanges(Map<String, Map<String, String>> viewRendererConfiguration) {
        final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.exchanges = builder.build();
    }

    @JsonProperty("database")
    public Map<String, String> getDatabase() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(Map<String, String> database){
        this.database = database;
    }

    
    @JsonProperty("logging")
    public Map<String, String> getLogging(){
    		return this.logging;
    }
    
    @JsonProperty("logging")
    public void setLogging(Map<String, String> log) {
    		this.logging = log;
    }
    
    public DatabaseConfiguration getDBConfiguration(){
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDriverClass(this.getDatabase().get("driverClasss"));
        config.setPassword(this.getDatabase().get("password"));
        config.setUrl(this.getDatabase().get("url"));
        config.setUsername(this.getDatabase().get("username"));
        return config;
    }
    
    
    
}