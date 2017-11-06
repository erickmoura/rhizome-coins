package hk.rhizome.coins.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hk.rhizome.coins.model.UserBalances;
import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;

public class UserBalancesDAO extends AbstractDAO<UserBalances> {
    
    public UserBalancesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    
    public List<UserBalances> findAll() {
        return list(namedQuery("hk.rhizome.coins.model.UserBalances.findAll"));
    }
    
    public List<UserBalances> findByUserID(int userID) {
        return list(
                namedQuery("hk.rhizome.coins.model.UserBalances.findByUserID")
                .setParameter("user_id", userID));
    }
    
    
    //TODO: review these queries in this context
    public List<UserBalances> findByUserID(int userID, Date collectDate){
        return list(namedQuery("hk.rhizome.coins.model.User.findBalances").
        setParameter("collect_date", collectDate).
        setParameter("user_id", userID));
    }


    public int create(UserBalances userBalance){
        return persist(userBalance).getID();
    }

    public List<Integer> create(List<UserBalances> list){
        List<Integer> ids = new ArrayList<Integer>();
        for(UserBalances ub : list){
            ids.add(persist(ub).getID());
        }
        return ids;
    }
    
}