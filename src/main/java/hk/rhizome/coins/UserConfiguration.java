package hk.rhizome.coins;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Map;

public class UserConfiguration {

    private String username;
    private Map<String, Object> exchanges;

    @JsonProperty("username")
    public String getUsername(){
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username){
        this.username = username;
    }

    @JsonProperty("exchanges")
    public Map<String, Object> getExchanges(){
        return exchanges;
    }
    
    @JsonProperty("exchanges")
    public void setExchanges(Map<String, Object> exchanges){
        this.exchanges = exchanges;
    }

}