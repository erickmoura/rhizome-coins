package hk.rhizome.coins.db;

import hk.rhizome.coins.model.Coins;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import java.util.List;

public class CoinsDAO extends AbstractDAO<CoinsDAO> {
    
    public CoinsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Coins> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.Coins.findAll"));
    }
    
}