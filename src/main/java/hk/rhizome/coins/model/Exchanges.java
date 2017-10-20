package hk.rhizome.coins.model;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "exchanges")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.Exchanges.findAll",
            query = "from Exchanges"),
            @NamedQuery(name = "hk.rhizome.coins.model.Exchanges.getByID",
            query = "from Exchanges where id like :id" )
})
public class Exchanges {
    
    @Id
    int id;
    
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
        this.id = id;
        this.exchangeName = exchangeName;
        this.xchangeName = xchangeName;
        this.taker = taker;
        this.maker = maker;
        this.pollingRate = pollingRate;
    }

	public int getID(){
        return this.id;
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