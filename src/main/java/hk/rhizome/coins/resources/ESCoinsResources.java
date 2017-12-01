package hk.rhizome.coins.resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.amazonaws.Response;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.service.ESCoinsService;

@Path("search")
@Produces(MediaType.APPLICATION_JSON)
public class ESCoinsResources {

    ESCoinsService service;
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    

    public ESCoinsResources(ESCoinsService service){
        this.service = service;
    }

    @GET
    @Path("/coinmarketcap")
    public List<Map<String, Object>> getCoinMarketCapData(@QueryParam("startDate") String startDate){
        JsonArray hitsJsonArray = new JsonArray();
        try{
            AppLogger.getLogger().info("Started retrieve CoinMarketCap data from ES");
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date startD = sdf.parse(startDate);
            Response<String> response = service.getCoinMarketCap(startD);
            String awsResponse = response.getAwsResponse();
            JsonObject jsonObject = new JsonParser().parse(awsResponse).getAsJsonObject();
            JsonObject hitsJsonObject = jsonObject.getAsJsonObject("hits");
            hitsJsonArray = hitsJsonObject.getAsJsonArray("hits");
            AppLogger.getLogger().info("Finished retrieve CoinMarketCap data from ES");
            
        }
        catch(Exception e){
            AppLogger.getLogger().error("Exception in ESCoinsResources", e);
            //throw e;
        }
        return extractResponseFromArray(hitsJsonArray);
	}

	List<Map<String, Object>> extractResponseFromArray(JsonArray hitsArray) {
		List<Map<String, Object>> response = new ArrayList<>();
		try {
			if (hitsArray != null) {
				for (JsonElement jsonElement : hitsArray) {
					Map<String, Object> map = new HashMap<>();
					JsonObject baseObject = jsonElement.getAsJsonObject();
					JsonObject source = baseObject.getAsJsonObject("_source");
					Map<String, Object> resultMap = new Gson().fromJson(source, map.getClass());
					JsonPrimitive score = baseObject.getAsJsonPrimitive("_score");
					
					resultMap.put("score", score.toString());
					response.add(resultMap);
				}
			}
		} catch (Exception e) {
			AppLogger.getLogger().error("Exception in :: ResultSetFormatter :: extractResponseFromArray()");
			throw e;
		}
		return response;
	}
}