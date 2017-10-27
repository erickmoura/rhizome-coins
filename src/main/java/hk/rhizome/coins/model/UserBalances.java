package hk.rhizome.coins.model;

import java.util.Date;
import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "user_balances")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.UserBalances.findAll",
            query = "from UserBalances"),
    @NamedQuery(name = "hk.rhizome.coins.model.UserBalances.findByUserID",
            query = "from UserBalances ub "
            + "where ub.userID like :user_id ")
})
public class UserBalances {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    
    @Column(name = "user_id")
    int userID;
    
    @Column(name = "exchange_id")
    int exchangeID;

    @Column(name = "currency")
    String currencyCode;

    @Column(name = "total")
    BigDecimal total;

    @Column(name = "available")
    BigDecimal available;

    @Column(name = "frozen")
    BigDecimal frozen;

    @Column(name = "loaned")
    BigDecimal loaned;

    @Column(name = "borrowed")
    BigDecimal borrowed;

    @Column(name = "withdrawing")
    BigDecimal withdrawing;

    @Column(name = "depositing")
    BigDecimal depositing;

    @Column(name = "collect_date")
    Date collectDate;

    
    public UserBalances(){

    }
    
    public UserBalances(int userID, int exchangeID, String currencyCode, BigDecimal total, BigDecimal available,  BigDecimal frozen, BigDecimal loaned, BigDecimal borrowed, BigDecimal withdrawing, BigDecimal depositing, Date collectDate){
        this.userID = userID;
        this.exchangeID = exchangeID;
        this.currencyCode = currencyCode;
        this.total = total;
        this.available = available;
        this.frozen = frozen;
        this.loaned = loaned;
        this.borrowed = borrowed;
        this.withdrawing = withdrawing;
        this.depositing = depositing;
        this.collectDate = collectDate;
    }

    
    public int getID(){
        return this.id;
    }
    public void setID(int id){
        this.id = id;
    }

    
    public int getUserID(){
        return this.userID;
    }
    public int getExchangeID(){
        return this.exchangeID;
    }
    public String getCurrency(){
        return this.currencyCode;
    }
    public BigDecimal getTotal(){
        return this.total;
    }
    public BigDecimal getAvailable(){
        return this.available;
    }
    public BigDecimal getFrozen(){
        return this.frozen;
    }
    public BigDecimal getLoaned(){
        return this.loaned;
    }
    public BigDecimal getBorrowed(){
        return this.borrowed;
    }
    public BigDecimal getWithdrawing(){
        return this.withdrawing;
    }
    public BigDecimal getDepositing(){
        return this.depositing;
    }
    public Date getCollectDate(){
        return this.collectDate;
    }
    
}
