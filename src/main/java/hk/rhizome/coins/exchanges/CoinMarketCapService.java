package hk.rhizome.coins.exchanges;

import java.util.List;
import si.mazi.rescu.RestProxyFactory;
import hk.rhizome.coins.logger.AppLogger;


public class CoinMarketCapService{

    private CoinMarketCap coinMarketCap;
    private CoinMarketCapExchange exchange;

    public CoinMarketCapService(){
        this.exchange = new CoinMarketCapExchange();
        this.coinMarketCap = RestProxyFactory.createProxy(CoinMarketCap.class, exchange.getURI());
    }

    public List<CoinMarketCapTicker> getTickers() throws Exception {
        AppLogger.getLogger().info("Get tickers in CoinMarkertcap");
        List<CoinMarketCapTicker> response = coinMarketCap.getTickers();
        return response;
    }
}