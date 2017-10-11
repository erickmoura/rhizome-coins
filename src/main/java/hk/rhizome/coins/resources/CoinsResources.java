package hk.rhizome.coins.resources;

import io.dropwizard.db.DataSourceFactory;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.db.DataSourceUtil;
import hk.rhizome.coins.db.QueryReader;


public class CoinsResources{
    
    DataSourceFactory dataSourceFactory;
    QueryReader queryReader;

    public CoinsResources(DataSourceFactory dataSourceFactory){
        this.dataSourceFactory = dataSourceFactory;
        this.queryReader = new QueryReader();
    }

    public List<Map<String, Object>> getCoins() throws Exception {

        List<Map<String, Object>> list;
        Handle handle = null;
        try {
            AppLogger.getLogger().debug("Started getCoins");

            DBI databaseConnection = DataSourceUtil.getInstance().getConnection(this.dataSourceFactory, "getCoins");
            if (databaseConnection == null) {
                AppLogger.getLogger().error("No able to use connection in CoinsResources in getCoins");
                throw new Exception("No able to use connection in CoinsResources in getCoins");
            }
            handle = databaseConnection.open();
            String sql = queryReader.getSQLCoins();
            list = handle.select(sql);
            
            AppLogger.getLogger().debug("Finished getCoins");
        } catch (Exception ex) {
            AppLogger.getLogger().error("Exception in CoinsResources in getCoins");
            throw ex;
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
        return list;
    }
}