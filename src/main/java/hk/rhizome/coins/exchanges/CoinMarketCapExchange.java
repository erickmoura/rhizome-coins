package hk.rhizome.coins.exchanges;

public class CoinMarketCapExchange {


    private String uri;

    public CoinMarketCapExchange(){
        configure();
    }

    public void configure(){
        this.uri = "https://api.coinmarketcap.com/";
    }

    public String getURI(){
        return this.uri;
    }

    public void setURI(String uri){
        this.uri = uri;
    }

}