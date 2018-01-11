package hk.rhizome.coins.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "user_name") })
@NamedQueries({ @NamedQuery(name = "hk.rhizome.coins.model.User.findAll", query = "from User"),
                @NamedQuery(name = "hk.rhizome.coins.model.User.findByName", query = "from User where name = :name"),
                @NamedQuery(name = "hk.rhizome.coins.model.User.findBalances", query = "select ub from User u inner join UserBalances ub on u.userID = ub.userID "
                                + "where u.userID = :user_id and " + "ub.collectDate = :collect_date"),
                @NamedQuery(name = "hk.rhizome.coins.model.User.findOrders", query = "select uo from User u inner join UserOrders uo on u.id = uo.userID "
                                + "where uo.userID = :user_id and "
                                + "uo.orderDate between :start_date and :end_date") })
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", unique = true, nullable = false)
        private int userID;

        @Column(name = "user_name")
        private String name;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "users_exchanges", joinColumns = {
                        @JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
                                        @JoinColumn(name = "exchange_id", nullable = false, updatable = false) })
        private Set<Exchanges> exchanges = new HashSet<Exchanges>(0);

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name="user_id")
        private Set<UserExchanges> userExchanges = new HashSet<UserExchanges>(0);

        @OneToMany
        @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
        @Cascade(CascadeType.ALL)
        private Set<UserBalances> balances = new HashSet<UserBalances>(0);

        @OneToMany
        @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
        @Cascade(CascadeType.ALL)
        private Set<UserOrders> orders = new HashSet<UserOrders>(0);

        @OneToMany
        @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
        @Cascade(CascadeType.ALL)
        private Set<UserTrades> trades = new HashSet<UserTrades>(0);

        public User() {

        }

        public User(int id, String name) {
                this.userID = id;
                this.name = name;
        }

        public int getID() {
                return this.userID;
        }

        public String getName() {
                return this.name;
        }

        public Set<Exchanges> getExchanges() {
                return this.exchanges;
        }

        public void setExchanges(Set<Exchanges> exchanges) {
                this.exchanges = exchanges;
        }

        public Set<UserBalances> getBalances(){
                return this.balances;
        }
        public void setBalances(Set<UserBalances> balances){
                this.balances = balances;
        }

        public Set<UserExchanges> getUserExchanges(){
                return this.userExchanges;
        }
        public void setUserExchanges(Set<UserExchanges> userExchanges){
                this.userExchanges = userExchanges;
        }

        public Set<UserBalances> getUserBalances(){
                return this.balances;
        }
        public void setUserBalances(Set<UserBalances> userBalances){
                this.balances = userBalances;
        }

        public Set<UserOrders> getOrders(){
                return this.orders;
        }
        public void setOrders(Set<UserOrders> userOrders){
                this.orders = userOrders;
        }

        public Set<UserTrades> getTrades(){
                return this.trades;
        }
        public void setTrades(Set<UserTrades> trades){
                this.trades = trades;
        }

}