package hk.rhizome.coins.db;

import java.util.List;
import java.util.Optional;
import hk.rhizome.coins.model.Exchanges;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class ExchangesDAO extends AbstractDAO<Exchanges> {
    
    public ExchangesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Exchanges> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.Exchanges.findAll"));
    }

    public List<Exchanges> getExchangeByUserID(int userID){
        return list(namedQuery("hk.rhizome.coins.model.Users.findExchanges").
        setParameter("user_id", userID));
    }
    
    public List<Exchanges> getExchangeByID(long id){
        return list(namedQuery("hk.rhizome.coins.model.Exchanges.getByID"));
    }

    public Exchanges getExchangeByName(String name){
        return (Exchanges) namedQuery("hk.rhizome.coins.model.Exchanges.getByName").setParameter("name",name).getSingleResult();
    }

}