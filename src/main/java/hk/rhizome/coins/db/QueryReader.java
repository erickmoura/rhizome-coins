package hk.rhizome.coins.db;

import java.io.File;

import hk.rhizome.coins.utils.RhizomeCoinsUtil;
import hk.rhizome.coins.logger.AppLogger;

public class QueryReader {

    private RhizomeCoinsUtil util;

    public QueryReader() {
        util = new RhizomeCoinsUtil();
    }

    public String getSQLExchanges() throws Exception {

        String sql;
        try {
            String fileName = "queries/exchanges.sql";
            File exchangeFile = util.getSqlFile(fileName);

            sql = RhizomeCoinsUtil.getSqlString(exchangeFile);

        } catch (Exception ex) {
            AppLogger.getLogger().error("Exception in QueryGenerator getSQLExchanges()");
            throw ex;
        }
        return sql;
    }

    public String getSQLCoins() throws Exception {
        String sql = "select * from coins where removed_date is null;";
        return sql;
    }

}