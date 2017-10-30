package hk.rhizome.coins.db;

import hk.rhizome.coins.model.UserOrders;
import io.dropwizard.hibernate.AbstractDAO;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

public class UserOrdersDAO extends AbstractDAO<UserOrders> {
    
    public UserOrdersDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<UserOrders> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.UserOrders.findAll"));
    }
    
    public List<UserOrders> getOrders(int userID, Date startDate, Date endDate){
        return list(namedQuery("hk.rhizome.coins.model.Users.findOrders").
        setParameter("start_date", startDate).
        setParameter("end_date", endDate).
        setParameter("user_id", userID));
    }
     
    public void create(UserOrders userOrders){
        persist(userOrders).getID();
    }

}