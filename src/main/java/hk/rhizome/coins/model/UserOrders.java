package hk.rhizome.coins.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "user_orders")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.UserOrders.findAll",
            query = "from UserOrders"),
    @NamedQuery(name = "hk.rhizome.coins.model.UserOrders.findByUserID",
            query = "from UserOrders ub "
            + "where ub.userID = :user_id ")
})
public class UserOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="order_id")
    private String orderID;

    @Column(name = "user_id")
    private int userID;

    @Column(name = "exchange_id")
    private int exchangeID;

    @Column(name = "currency")
    private String currency;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "order_status")
    private String orderStatus;
    
    @Column(name = "tradable_amount")
    private BigDecimal tradableAmount;
    
    @Column(name = "cumlative_amount")
    private BigDecimal cumlativeAmount;
    
    @Column(name = "average_price")
    private BigDecimal averagePrice;

    @Column(name = "order_date")
    private Date orderDate;

    public UserOrders(){
        
    }

    public UserOrders(String orderID, int userID, int exchangeID, String currency, String orderType, String orderStatus, BigDecimal tradableAmount, BigDecimal cumlativeAmount, BigDecimal averagePrice,  Date orderDate){
        this.orderID = orderID;
        this.userID = userID;
        this.exchangeID = exchangeID;
        this.currency = currency;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.tradableAmount = tradableAmount;
        this.cumlativeAmount = cumlativeAmount;
        this.averagePrice = averagePrice;
        this.orderDate = orderDate;
    }

    public int getID(){
        return this.id;
    }
    public String getOrderID(){
        return this.orderID;
    }
    public int getUserID(){
        return this.userID;
    }
    public int getExchangeID(){
        return this.exchangeID;
    }
    public String getCurrency(){
        return this.currency;
    }
    public String getOrderType(){
        return this.orderType;
    }
    public String getOrderStatus(){
        return this.orderStatus;
    }
    public BigDecimal getTradableAmount(){
        return this.tradableAmount;
    }
    public BigDecimal getCumlativeAmount(){
        return this.cumlativeAmount;
    }
    public BigDecimal getAveragePrice(){
        return this.averagePrice;
    }
    public Date getOrderDate(){
        return orderDate;
    }
    
}
