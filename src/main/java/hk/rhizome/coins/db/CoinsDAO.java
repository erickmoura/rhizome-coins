package hk.rhizome.coins.db;

import java.util.List;

import org.hibernate.SessionFactory;

import hk.rhizome.coins.model.Coins;
import io.dropwizard.hibernate.AbstractDAO;

public class CoinsDAO extends AbstractDAO<Coins> {
    
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