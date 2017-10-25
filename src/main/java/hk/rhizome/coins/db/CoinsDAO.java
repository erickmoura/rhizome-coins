package hk.rhizome.coins.db;

import hk.rhizome.coins.model.Coins;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import java.util.List;
import java.util.Optional;

public class CoinsDAO extends AbstractDAO<CoinsDAO> {
    
    public CoinsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Coins> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.Coins.findAll"));
    }

    public Coins findByName(String name){
        return (Coins) namedQuery("hk.rhizome.coins.model.Coins.findByName")
        .setParameter("name", name).getSingleResult();
    }
    
}