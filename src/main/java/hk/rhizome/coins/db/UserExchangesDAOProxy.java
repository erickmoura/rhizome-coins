package hk.rhizome.coins.db;

import java.util.List;

import hk.rhizome.coins.model.UserExchanges;
import io.dropwizard.hibernate.UnitOfWork;

public class UserExchangesDAOProxy{

    private UserExchangesDAO dao;

    public UserExchangesDAOProxy(UserExchangesDAO dao){
        this.dao = dao;
    }

    @UnitOfWork
    public List<UserExchanges> getExchangesByUser(int userID){
        return dao.findByUserID(userID);
    }

    @UnitOfWork
    public List<UserExchanges> getAllUserExchanges(){
        return dao.findAll();
    }

    @UnitOfWork
    public UserExchanges getUserExchanges(int userID, int exchangeID){
        return dao.findUserExchange(userID, exchangeID);
    }

    @UnitOfWork
    public void update(UserExchanges userExchanges){
        dao.update(userExchanges);
    }

}