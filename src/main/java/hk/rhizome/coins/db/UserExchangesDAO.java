package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.model.UserExchanges;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class UserExchangesDAO extends AbstractDAO<UserExchanges> {
    
    public UserExchangesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<UserExchanges> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.UserExchanges.findAll"));
    }
    
    public List<UserExchanges> findByUserID(int userID) {
        return list(
                namedQuery("hk.rhizome.coins.model.UserExchanges.findByUserID")
                .setParameter("user_id", userID));
    }

    public UserExchanges findUserExchange(int userID, int exchangeID){
        return (UserExchanges)namedQuery("hk.rhizome.coins.model.UserExchanges.findByUserIDExchangeID")
        .setParameter("user_id", userID).setParameter("exchange_id", exchangeID).uniqueResult();
    }

    public void update(UserExchanges userExchanges) {
        currentSession().saveOrUpdate(userExchanges);
    }
 
}