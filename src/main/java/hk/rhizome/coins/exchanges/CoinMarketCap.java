package hk.rhizome.coins.exchanges;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("v1")
public interface CoinMarketCap {
    
    @GET
    @Path("/ticker/")
    ArrayList<CoinMarketCapTicker> getTickers() throws IOException;
  
}