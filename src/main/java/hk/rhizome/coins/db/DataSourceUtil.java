package hk.rhizome.coins.db;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import java.util.Map;

import org.skife.jdbi.v2.DBI;

import hk.rhizome.coins.logger.AppLogger;

public class DataSourceUtil {

    private static final Object LOCK = new Object();
    
    private static DataSourceUtil singleton;

    private Environment environment;

    private static final String DRIVER_CLASS = "driverClass";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String URL = "url";

    public DataSourceUtil(Environment environment){
        this.environment = environment;
    }

    public static DataSourceUtil getInstance() {
        if(singleton == null)
            AppLogger.getLogger().error("DataSourceUtil not initialized");
        return singleton;
    }
    
    public static DataSourceUtil initialize(Environment environment) {
        synchronized (LOCK) {
            if (singleton == null) {
                singleton = new DataSourceUtil(environment);
            }
        }
        return singleton;
    }
 

    /**
     * Creates a data source factory given a configuration map.
     *
     * @param configuration the configuration.
     * @return the data source factory.
     */
    public static DataSourceFactory getDataSourceFactory(Map<String, String> configuration) {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass(configuration.get(DRIVER_CLASS));
        dataSourceFactory.setPassword(configuration.get(PASSWORD));
        dataSourceFactory.setUser(configuration.get(USER));
        dataSourceFactory.setUrl(configuration.get(URL));
        return dataSourceFactory;
    }

    public DBI getConnection(DataSourceFactory dataSourceFactory, String name){
        DBIFactory dbiFactory = new DBIFactory();
        DBI dbi = dbiFactory.build(this.environment, dataSourceFactory, name);
        return dbi;
        
    }
   
}
