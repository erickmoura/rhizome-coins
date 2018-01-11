package hk.rhizome.coins.db;

import java.util.List;

import hk.rhizome.coins.model.Exchanges;
import io.dropwizard.hibernate.UnitOfWork;

public class ExchangesDAOProxy {
    
      private ExchangesDAO exchangesDAO;
    
      public ExchangesDAOProxy(ExchangesDAO exchangesDAO) {
        this.exchangesDAO = exchangesDAO;
      }
    
      @UnitOfWork
      public List<Exchanges> getAllExchanges() {
        return exchangesDAO.findAll();
      }

      @UnitOfWork
      public List<Exchanges> getExchangeByID(int id){
        return exchangesDAO.getExchangeByID(id);
      }
}