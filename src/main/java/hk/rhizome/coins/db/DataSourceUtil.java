package hk.rhizome.coins.db;

import io.dropwizard.db.DataSourceFactory;

import java.util.Map;

public class DataSourceUtil {

    private static final String DRIVER_CLASS = "driverClass";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String URL = "url";

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
   
}
