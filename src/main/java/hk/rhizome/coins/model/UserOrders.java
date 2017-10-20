package hk.rhizome.coins.model;

import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.currency.Currency;

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
    int id;

    @Column(name="order_id")
    String orderID;

    @Column(name = "user_id")
    int userID;

    @Column(name = "exchange_id")
    int exchangeID;

    @Column(name = "currency")
    String currency;

    @Column(name = "order_type")
    String orderType;

    @Column(name = "order_status")
    String orderStatus;
    
    @Column(name = "tradable_amount")
    BigDecimal tradableAmount;
    
    @Column(name = "cumlative_amount")
    BigDecimal cumlativeAmount;
    
    @Column(name = "average_price")
    BigDecimal averagePrice;

    @Column(name = "order_date")
    Date orderDate;

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
