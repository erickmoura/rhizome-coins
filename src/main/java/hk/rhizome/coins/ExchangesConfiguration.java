package hk.rhizome.coins;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Map;

public class ExchangesConfiguration {

    private Map<String, Object> exchanges;

    @JsonProperty("exchanges")
    public Map<String, Object> getExchanges(){
        return exchanges;
    }
    
    @JsonProperty("exchanges")
    public void setExchanges(Map<String, Object> exchanges){
        this.exchanges = exchanges;
    }

}