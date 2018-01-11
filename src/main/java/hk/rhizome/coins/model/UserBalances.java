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
    private int id;
    
    @Column(name = "user_id")
    private int userID;
    
    @Column(name = "exchange_id")
    private int exchangeID;

    @Column(name = "currency")
    private String currencyCode;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "available")
    private BigDecimal available;

    @Column(name = "frozen")
    private BigDecimal frozen;

    @Column(name = "loaned")
    private BigDecimal loaned;

    @Column(name = "borrowed")
    private BigDecimal borrowed;

    @Column(name = "withdrawing")
    private BigDecimal withdrawing;

    @Column(name = "depositing")
    private BigDecimal depositing;

    @Column(name = "collect_date")
    private Date collectDate;

    
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
