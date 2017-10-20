package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.model.UserOrders;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class UserOrdersDAO extends AbstractDAO<UserOrders> {
    
    public UserOrdersDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<UserOrders> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.UserOrders.findAll"));
    }
   
    public void create(UserOrders userOrders){
        persist(userOrders).getID();
    }

}