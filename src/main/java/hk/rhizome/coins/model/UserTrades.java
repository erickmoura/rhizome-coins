package hk.rhizome.coins.model;

import java.math.BigDecimal;
import java.util.Date;
import org.knowm.xchange.currency.Currency;

import javax.persistence.*;

@Entity
@Table(name = "user_trades")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.UserTrades.findAll",
            query = "from UserTrades"),
    @NamedQuery(name = "hk.rhizome.coins.model.UserTrades.findByUserID",
            query = "from UserTrades ub "
            + "where ub.userID = :user_id ")
})
public class UserTrades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "user_id")
    int userID;

    @Column(name = "exchange_id")
    int exchangeID;

    @Column(name="trade_id")
    String tradeID;

    @Column(name="order_id")
    String orderID;

    @Column(name="currency_pair")
    String currencyPair;

    @Column(name = "fee_amount")
    BigDecimal feeAmount;

    @Column(name = "fee_currency")
    String feeCurrency;

    @Column(name = "tradable_amount")
    BigDecimal tradableAmount;
    
    @Column(name = "price")
    BigDecimal price;
    
    @Column(name = "trade_date")
    Date tradeDate;
    
    @Column(name = "trade_type")
    String tradeType;

    public UserTrades(){
        
    }

    public UserTrades(int userID, int exchangeID, String tradeID, String orderID, String currency, BigDecimal feeAmount, String feeCurrency, BigDecimal tradableAmount, BigDecimal price, Date tradeDate, String tradeType){
        this.userID = userID;
        this.exchangeID = exchangeID;
        this.tradeID = tradeID;
        this.orderID = orderID;
        this.currencyPair = currency;
        this.feeAmount = feeAmount;
        this.feeCurrency = feeCurrency;
        this.tradableAmount = tradableAmount;
        this.price = price;
        this.tradeDate = tradeDate;
        this.tradeType = tradeType;
    }

    public String getTradeID(){
        return this.tradeID;
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
        return this.currencyPair;
    }
    public BigDecimal getFeeAmount(){
        return this.feeAmount;
    }
    public String getFeeCurrency(){
        return feeCurrency;
    }
    public BigDecimal getTradableAmount(){
        return tradableAmount;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public Date getTradeDate(){
        return tradeDate;
    }
    public String getTradeType(){
        return tradeType;
    }
}
