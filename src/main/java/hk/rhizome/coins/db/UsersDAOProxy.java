package hk.rhizome.coins.db;

import java.util.List;

import org.hibernate.Hibernate;

import hk.rhizome.coins.model.User;
import io.dropwizard.hibernate.UnitOfWork;

public class UsersDAOProxy{

    private UsersDAO dao;

    public UsersDAOProxy(UsersDAO dao){
        this.dao = dao;
    }

    @UnitOfWork
    public List<User> getAllUsers(){
        List<User> users = dao.findAll();
        for(User u : users){
            Hibernate.initialize(u.getExchanges());
            Hibernate.initialize(u.getUserExchanges());
        }
        return users;
    }

    @UnitOfWork
    public User getUsersByName(String name){
        User u = dao.findByName(name);
        Hibernate.initialize(u.getExchanges());
        Hibernate.initialize(u.getUserExchanges());
        return u;
    }

    @UnitOfWork
    public void saveUser(User user){
        dao.update(user);
    }

}