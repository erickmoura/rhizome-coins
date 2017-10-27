package hk.rhizome.coins.db;

import java.util.Date;
import java.util.List;
import hk.rhizome.coins.model.Exchanges;
import hk.rhizome.coins.model.UserBalances;
import hk.rhizome.coins.model.UserOrders;
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

    public List<UserBalances> getBalances(int userID, Date collectDate){
        return list(namedQuery("hk.rhizome.coins.model.Users.findBalances").
        setParameter("collect_date", collectDate).
        setParameter("user_id", userID));
    }

    public List<UserOrders> getOrders(int userID, Date startDate, Date endDate){
        return list(namedQuery("hk.rhizome.coins.model.Users.findOrders").
        setParameter("start_date", startDate).
        setParameter("end_date", endDate).
        setParameter("user_id", userID));
    }

    public List<Exchanges> getExchanges(int userID){
        return list(namedQuery("hk.rhizome.coins.model.Users.findExchanges").
        setParameter("user_id", userID));
    }
    
}