package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.model.Users;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class UsersDAO extends AbstractDAO<UsersDAO> {
    
    public UsersDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<Users> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.Users.findAll"));
    }
    
    public Users findByName(String name) {
        return (Users)namedQuery("hk.rhizome.coins.model.Users.findByName").setParameter("name", name).uniqueResult();
    }
    
}