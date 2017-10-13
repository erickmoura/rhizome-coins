package hk.rhizome.coins.exchanges;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.ArrayList;

@Path("v1")
public interface CoinMarketCap {
    
    @GET
    @Path("/ticker/")
    ArrayList<CoinMarketCapTicker> getTickers() throws IOException;
  
}