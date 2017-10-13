package hk.rhizome.coins.resources;

import io.dropwizard.db.DataSourceFactory;
import java.util.List;
import java.util.Map;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import hk.rhizome.coins.logger.AppLogger;
import hk.rhizome.coins.db.DataSourceUtil;
import hk.rhizome.coins.db.QueryReader;


public class ExchangesResources{
    
    DataSourceFactory dataSourceFactory;
    QueryReader queryReader;

    public ExchangesResources(DataSourceFactory dataSourceFactory){
        this.dataSourceFactory = dataSourceFactory;
        this.queryReader = new QueryReader();
    }

    public List<Map<String, Object>> getExchanges() throws Exception {

        List<Map<String, Object>> list;
        Handle handle = null;
        try {
            AppLogger.getLogger().debug("Started getExchanges");

            DBI databaseConnection = DataSourceUtil.getInstance().getConnection(this.dataSourceFactory, "getExchanges");
            if (databaseConnection == null) {
                AppLogger.getLogger().error("No able to use connection in ExchangesResources in getExchanges");
                throw new Exception("No able to use connection in ExchangesResources in getExchanges");
            }
            handle = databaseConnection.open();
            String sql = queryReader.getSQLExchanges();
            list = handle.select(sql);

            AppLogger.getLogger().debug("Finished getExchanges");
        } catch (Exception ex) {
            AppLogger.getLogger().error("Exception in ExchangesResources in getExchanges");
            throw ex;
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
        return list;
    }
}