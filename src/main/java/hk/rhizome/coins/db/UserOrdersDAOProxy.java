
package hk.rhizome.coins.db;

import hk.rhizome.coins.model.UserOrders;
import io.dropwizard.hibernate.UnitOfWork;

public class UserOrdersDAOProxy{

    private UserOrdersDAO dao;

    public UserOrdersDAOProxy(UserOrdersDAO dao){
        this.dao = dao;
    }

    @UnitOfWork
    public void create(UserOrders userOrders){
        dao.create(userOrders);
    }

}