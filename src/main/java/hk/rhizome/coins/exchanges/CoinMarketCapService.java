package hk.rhizome.coins.exchanges;

import java.util.List;

import hk.rhizome.coins.logger.AppLogger;
import si.mazi.rescu.RestProxyFactory;


public class CoinMarketCapService{

    private CoinMarketCap coinMarketCap;
    private CoinMarketCapExchange coinMarketCapExchange;

    public CoinMarketCapService(){
        this.coinMarketCapExchange = new CoinMarketCapExchange();
        this.coinMarketCap = RestProxyFactory.createProxy(CoinMarketCap.class, coinMarketCapExchange.getURI());
    }

    public List<CoinMarketCapTicker> getTickers() throws Exception {
        AppLogger.getLogger().info("Get tickers in CoinMarkertcap");
        List<CoinMarketCapTicker> response = coinMarketCap.getTickers();
        return response;
    }
}