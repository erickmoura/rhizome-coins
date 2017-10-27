package hk.rhizome.coins;

import hk.rhizome.coins.db.DbProxyUtils;
import hk.rhizome.coins.marketdata.FeesMatrix;
import hk.rhizome.coins.marketdata.TradingFeePair;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserExchanges;
import hk.rhizome.coins.model.Users;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class ExchangeUtils {

  private static ExchangeUtils singleton;

  private List<Exchanges> exchanges;
  private List<Exchange> botExchanges;

  private ExchangeUtils() {
    botExchanges = new ArrayList<Exchange>();
  }

  public void initialize() {
    exchanges =  DbProxyUtils.getInstance().getExchangesProxy().getAllExchanges();
    Users botUser = DbProxyUtils.getInstance().getUsersProxy().getUsersByName("bot");
    createBotData(botUser, DbProxyUtils.getInstance().getUserExchangesProxy().getExchangesByUser(botUser.getID()));
  }

  public List<Exchanges> AllExchanges(){
    return this.exchanges;
  }

  public static ExchangeUtils getInstance() {
    if (singleton == null) {
      singleton = new ExchangeUtils();

    }
    return singleton;
  }

  private void createBotData(Users u, List<UserExchanges> userExchanges) {
    for (Exchanges ex : exchanges) {
      for (UserExchanges ue : userExchanges) {
        if (ex.getID() == ue.getExchangeID()) {
          
          ExchangeSpecification spec = new ExchangeSpecification(ex.getXchangeName());
          spec.setApiKey(ue.getKey());
          spec.setSecretKey(ue.getSecret());
          FeesMatrix.setFeesMatrix(ex.getXchangeName(), new TradingFeePair(ex.getMaker(), ex.getTaker()));
          
          botExchanges.add(ExchangeFactory.INSTANCE.createExchange(spec));
        }
      }
    }
  }

  public Set<String> getExchangeClassNames() {
    Set<String> xchanges = new HashSet<String>();
    for (Exchanges ex : exchanges) {
      xchanges.add(ex.getXchangeName());
    }
    return xchanges;
  }

  /*
  * return the xchange exchange
  */
  public Exchange getExchange(String exchangeClassName) {
    
    for (Exchanges ex : exchanges) {
      if (ex.getExchangeName().compareTo(exchangeClassName) == 0) {
        ExchangeSpecification spec = new ExchangeSpecification(ex.getXchangeName());
        return ExchangeFactory.INSTANCE.createExchange(spec);
      }
    }
    return null;
  }

  public List<Exchange> getBotExchanges() {
    return this.botExchanges;
  }

  public Integer getExchangePollingRate(String exchangeClassName) {
    for (Exchanges ex : exchanges) {
      if (ex.getXchangeName().equals(exchangeClassName)) {
        return ex.getPollingRate();
      }
    }
    return null;
  }

  public Exchange getExchange(UserExchanges ue) {
    for (Exchanges ex : exchanges) {
      if (ex.getID() == ue.getExchangeID()) {
        ExchangeSpecification spec = new ExchangeSpecification(ex.getXchangeName());
        spec.setApiKey(ue.getKey());
        spec.setSecretKey(ue.getSecret());
        FeesMatrix.setFeesMatrix(ex.getXchangeName(), new TradingFeePair(ex.getMaker(), ex.getTaker()));
        return ExchangeFactory.INSTANCE.createExchange(spec);
      }
    }
    return null;
  }
}
