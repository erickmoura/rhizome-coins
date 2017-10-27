package hk.rhizome.coins.db;

import java.util.List;
import hk.rhizome.coins.model.Users;
import io.dropwizard.hibernate.UnitOfWork;

public class UsersDAOProxy{

    private UsersDAO dao;

    public UsersDAOProxy(UsersDAO dao){
        this.dao = dao;
    }

    @UnitOfWork
    public List<Users> getAllUsers(){
        return dao.findAll();
    }

    @UnitOfWork
    public Users getUsersByName(String name){
        return dao.findByName(name);
    }

}