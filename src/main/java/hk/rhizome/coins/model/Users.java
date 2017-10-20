package hk.rhizome.coins.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findAll",
            query = "from Users"),
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findByName",
            query = "from Users where name = :name"),
})
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @Column(name = "user_name")
    String name;

    public Users(){
        
    }
    public Users(int id, String name){
        this.id = id;
        this.name = name;
    }

	public int getID(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
}