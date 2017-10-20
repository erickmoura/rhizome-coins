package hk.rhizome.coins.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "user_exchanges")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findAll",
            query = "from UserExchanges"),
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findByUserID",
            query = "from UserExchanges ub "
            + "where ub.userID = :user_id "),
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findByUserIDExchangeID",
            query = "from UserExchanges ub "
            + "where ub.userID = :user_id and ub.exchangeID = :exchange_id")
})
public class UserExchanges {

    @Id
    int id;

    @Column(name = "user_id")
    int userID;
    
    @Column(name = "exchange_id")
    int exchangeID;
    
    @Column(name = "properties")
    String properties;
    
    @Column(name = "p_key")
    String pKey;
    
    @Column(name = "secret")
    String secret;

    @Column(name = "last_updated_balances")
    Date lastUpdatedBalances;

    @Column(name = "last_updated_orders")
    Date lastUpdatedOrders;


    public UserExchanges(){
        
    }

    public UserExchanges(int userID, int exchangeID, String properties, String pKey, String secret, Date lastUpdatedBalances, Date lastUpdatedOrders){
        this.userID = userID;
        this.exchangeID = exchangeID;
        this.properties = properties;
        this.pKey = pKey;
        this.secret = secret;
        this.lastUpdatedBalances = lastUpdatedBalances;
        this.lastUpdatedOrders = lastUpdatedOrders;
    }

    public int getID(){
        return this.id;
    }

    public int getUserID(){
        return this.userID;
    }
    public int getExchangeID(){
        return this.exchangeID;
    }
    public String getProperties(){
        return this.properties;
    }
    public String getKey(){
        return this.pKey;
    }
    public String getSecret(){
        return this.secret;
    }
    public Date getLastUpdatedBalances(){
        return this.lastUpdatedBalances;
    }
    public void setLastUpdatedBalances(Date date){
        this.lastUpdatedBalances = date;
    }
    public Date getLastUpdatedOrders(){
        return this.lastUpdatedOrders;
    }
    public void setLastUpdatedOrders(Date date){
        this.lastUpdatedOrders = date;
    }
}