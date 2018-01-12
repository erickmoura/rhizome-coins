package hk.rhizome.coins.marketdata;

import java.util.List;
import java.util.ArrayList;
import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.model.Coins;

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

    public void initialize() throws Exception{
        try{
            coins = DbProxyUtils.getInstance().getCoinsProxy().getAllCoins();
        }
        catch(Exception ex){
            AppLogger.getLogger().error("Error in CoinsSetService : initialize " +  ex.getLocalizedMessage());
            throw ex;
        }
    }

    public List<Coins> getCoins(){
        return this.coins;
    }

}