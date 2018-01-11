package hk.rhizome.coins.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "users_exchanges")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findAll",
            query = "from UserExchanges"),
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findByUserID",
            query = "from UserExchanges ub "
            + "where ub.user.userID = :user_id "),
    @NamedQuery(name = "hk.rhizome.coins.model.UserExchanges.findByUserIDExchangeID",
            query = "from UserExchanges ub "
            + "where ub.user.userID = :user_id and ub.exchange.exchangeID = :exchange_id")
})
public class UserExchanges {

    @EmbeddedId
    private Id id = new Id();

    //TODO: Omaida, see the right way to make mappings/relationships and avoid
    //cumbersome Util methods, line in ExchangeUtils
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
    User user;
    
    @ManyToOne
    @JoinColumn(name="exchange_id", nullable=false, insertable=false, updatable=false)
    Exchanges exchange;

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

    public UserExchanges(User user, Exchanges exchange,  String properties, String pKey, String secret, Date lastUpdatedBalances, Date lastUpdatedOrders){
        this.user = user;
        this.exchange = exchange;
        this.properties = properties;
        this.pKey = pKey;
        this.secret = secret;
        this.lastUpdatedBalances = lastUpdatedBalances;
        this.lastUpdatedOrders = lastUpdatedOrders;

        this.id.exchangeID = exchange.getID();
        this.id.userID = user.getID();
    }

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "user_id")
        private int userID;
        
        @Column(name = "exchange_id")
        private int exchangeID;

        public Id() {
        }

        public Id(int userID, int exchangeID) {
            this.userID = userID;
            this.exchangeID = exchangeID;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.exchangeID == that.exchangeID && this.userID == that.userID;
            } else {
                return false;
            }
        }

    }
    
    public User getUser(){
        return this.user;
    }
    public Exchanges getExchange(){
        return this.exchange;
    }
    public void setExchange(Exchanges exchanges){
        this.exchange = exchanges;
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