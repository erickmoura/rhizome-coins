package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.db.UserBalancesDAO;
import hk.rhizome.coins.model.UserBalances;
import io.dropwizard.hibernate.UnitOfWork;

public class UserBalancesDAOProxy{

    private UserBalancesDAO dao;

    public UserBalancesDAOProxy(UserBalancesDAO dao){
        this.dao = dao;
    }

    @UnitOfWork
    public List<Integer> create(List<UserBalances> list){
        return dao.create(list);
    }

    @UnitOfWork
    public int create(UserBalances userBalance){
        return dao.create(userBalance);
    }

}