package hk.rhizome.coins.model;

import java.math.BigDecimal;
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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "exchanges", uniqueConstraints = {
            @UniqueConstraint(columnNames = "exchange_name")})
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.Exchanges.findAll",
            query = "from Exchanges"),
            @NamedQuery(name = "hk.rhizome.coins.model.Exchanges.getByID",
            query = "from Exchanges where exchangeID = :id" )
})
public class Exchanges {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_id", unique = true, nullable = false)
    int exchangeID;
    
    @Column(name = "exchange_name")
    String exchangeName;

    @Column(name = "xchange_name")
    String xchangeName;

    @Column(name = "taker")
    BigDecimal taker;

    @Column(name = "maker")
    BigDecimal maker;

    @Column(name = "polling_rate")
    int pollingRate;

    public Exchanges(){
        
    }

    public Exchanges(int id, String exchangeName, String xchangeName, BigDecimal taker,  BigDecimal maker, int pollingRate){
        this.exchangeID = id;
        this.exchangeName = exchangeName;
        this.xchangeName = xchangeName;
        this.taker = taker;
        this.maker = maker;
        this.pollingRate = pollingRate;
    }

	public int getID(){
        return this.exchangeID;
    }
    public String getExchangeName(){
        return this.exchangeName;
    }
    public String getXchangeName(){
        return this.xchangeName;
    }
    public BigDecimal getTaker(){
        return this.taker;
    }
    public BigDecimal getMaker(){
        return this.maker;
    }
    public int getPollingRate(){
        return this.pollingRate;
    }

    @Override
    public String toString(){
        return "Exchanges { id: " + this.getID() + ", exchangeName: " + this.getExchangeName() + ", XchangeName: "
                + ", taker: " + getTaker() + ", maker: " + getMaker() + ", polling rate: " + getPollingRate() + " }";
    }

}