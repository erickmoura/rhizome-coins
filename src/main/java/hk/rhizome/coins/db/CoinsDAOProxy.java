package hk.rhizome.coins.db;

import java.util.List;

import hk.rhizome.coins.model.Coins;
import io.dropwizard.hibernate.UnitOfWork;

public class CoinsDAOProxy {
    
      private CoinsDAO coinsDAO;
    
      public CoinsDAOProxy(CoinsDAO coinsDAO) {
        this.coinsDAO = coinsDAO;
      }
    
      @UnitOfWork
      public List<Coins> getAllCoins() {
        return coinsDAO.findAll();
      }
    }