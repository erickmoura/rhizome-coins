package hk.rhizome.coins.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findAll",
            query = "from Users"),
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findByName",
            query = "from Users where name = :name"),
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findExchanges",
            query = "select e from Exchanges e inner join UserExchanges ue on e.id = ue.exchangeID "
                    + "where ue.userID = :user_id" ),
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findBalances",
            query = "select ub from Users u inner join UserBalances ub on u.id = ub.userID "
            + "where u.id = :user_id and "
            + "ub.collectDate = :collect_date"),
    @NamedQuery(name = "hk.rhizome.coins.model.Users.findOrders",
            query = "select uo from Users u inner join UserOrders uo on u.id = uo.userID "
            + "where uo.userID = :user_id and "
            + "uo.orderDate between :start_date and :end_date")
})
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @Column(name = "user_name")
    String name;

    private Set<Exchanges> exchanges = new HashSet<Exchanges>(0);

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
    
    //TODO: Omaida, you may need to adjust this. I haven't tested it.
    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_exchanges",joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "exchange_id",
					nullable = false, updatable = false) })
	public Set<Exchanges> getExchanges() {
		return this.exchanges;
	}
}