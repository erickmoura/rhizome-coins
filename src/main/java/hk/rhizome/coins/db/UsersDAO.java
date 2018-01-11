package hk.rhizome.coins.db;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import hk.rhizome.coins.model.User;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
import io.dropwizard.hibernate.AbstractDAO;

public class UsersDAO extends AbstractDAO<User> {
    
    public UsersDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<User> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.User.findAll"));
    }
    
    public User findByName(String name) {
        return (User)namedQuery("hk.rhizome.coins.model.User.findByName").setParameter("name", name).uniqueResult();
    }

    public User getByID(int id){
        return get(id);
    }

    public void update(User user){
        persist(user);
    }

    
    public List<UserBalances> getBalances(User user, Date collectDate){
        return list(namedQuery("hk.rhizome.coins.model.User.findBalances").
        setParameter("collect_date", collectDate).
        setParameter("user_id", user.getID()));
    }

    public List<UserOrders> getOrders(User user, Date startDate, Date endDate){
        return list(namedQuery("hk.rhizome.coins.model.User.findOrders").
        setParameter("start_date", startDate).
        setParameter("end_date", endDate).
        setParameter("user_id", user.getID()));
    }
    /*
    public List<Exchanges> getExchanges(int userID){
        return list(namedQuery("hk.rhizome.coins.model.User.findExchanges").
        setParameter("user_id", userID));
    }
    */
}