package hk.rhizome.coins.marketdata;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Coins;
import hk.rhizome.coins.resources.CoinsResources;
import io.dropwizard.db.DataSourceFactory;

public class CoinsSetService {

    private List<Coins> coins;

    private static CoinsSetService singleton;
    
    private CoinsSetService(){
        coins = new ArrayList<Coins>();
    }

    public static CoinsSetService getInstance(){
        if (singleton == null){
            singleton = new CoinsSetService();
        }
        return singleton;
    }

    public void initialize(DataSourceFactory dataSourceFactory) throws Exception{
        try{
            CoinsResources resource = new CoinsResources(dataSourceFactory);
            coins = convertToList(resource.getCoins());
        }
        catch(Exception ex){
            AppLogger.getLogger().error("Error in CoinsSetService : initialize" +  ex.getLocalizedMessage());
            throw ex;
        }
    }

    public List<Coins> getCoins(){
        AppLogger.getLogger().info("cc coins");
        return this.coins;
    }

    private List<Coins> convertToList(List<Map<String, Object>> data){
        List<Coins> l = new ArrayList<Coins>();
        for(Map<String,Object> d : data){
            l.add(new Coins((String)d.get("id"), (String)d.get("coin_name"), (String)d.get("symbol"),(Date) d.get("inserted_date"), (Date)d.get("removed_date")));
        }
        return l;
    }
}