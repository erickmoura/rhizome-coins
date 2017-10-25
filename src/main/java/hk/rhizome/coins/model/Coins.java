package hk.rhizome.coins.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "coins")
@NamedQueries({
    @NamedQuery(name = "hk.rhizome.coins.model.Coins.findAll",
            query = "from Coins"),
    @NamedQuery(name = "hk.rhizome.coins.model.Coins.findByName",
            query = "from Coins where name = :name")
})
public class Coins {
    
    @Id
    String id;
    
    @Column(name = "coin_name")
    String name;

    @Column(name = "symbol")
    String symbol;

    @Column(name = "inserted_date")
    Date insertedDate;

    @Column(name = "removed_date")
    Date removedDate;

    public Coins(){
        
    }

    public Coins(String id, String name, String symbol, Date insertedDate, Date removedDate){
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.insertedDate = insertedDate;
        this.removedDate = removedDate;
    }

	public String getID(){
        return this.id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public Date getInsertedDate(){
        return this.insertedDate;
    }

    public void setInsertedDate(Date dateInserted){
        this.insertedDate = dateInserted;
    }

    public Date getRemovedDate(){
        return this.removedDate;
    }

    public void setRemovedDate(Date removedDate){
        this.removedDate = removedDate;
    }

    @Override
    public String toString() {
        return "Coins : {" + "id: " + getID() + ", name: "+ getName() + ", symbol: " + getSymbol() + 
                ", insertedDate : "+getInsertedDate() + ", removedDate : "+ getRemovedDate() + "} ";
    }
}