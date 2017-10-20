package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.model.Exchanges;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class ExchangesDAO extends AbstractDAO<ExchangesDAO> {
    
    public ExchangesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Exchanges> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.Exchanges.findAll"));
    }

    public List<Exchanges> getExchangeByID(long id){
        return list(namedQuery("hk.rhizome.coins.model.Exchanges.getByID"));
    }

}