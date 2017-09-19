package hk.rhizome.coins; 
/**
 * Created by erickmoura on 28/7/2017.
 *
 */

import com.google.common.collect.ImmutableMap;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;


public class RhizomeCoinsConfiguration extends Configuration {
    
    @NotNull
    private Map<String, Map<String, String>> exchanges = Collections.emptyMap();

    @NotNull
    private Map<String, String> database = Collections.emptyMap();

    @JsonProperty("exchanges")
    public Map<String, Map<String, String>> getExchanges() {
        return exchanges;
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

}