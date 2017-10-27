package hk.rhizome.coins.account;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import java.math.BigDecimal;

/**
 * Created by erickmoura on 12/8/2017.
 */
public class ExchangeBalance {

    private final String exchange;

    private final Currency currency;

    // Invariant:
    // total = available + frozen - borrowed + loaned + withdrawing + depositing;
    private final BigDecimal total;
    private final BigDecimal available;
    private final BigDecimal frozen;
    private final BigDecimal loaned;
    private final BigDecimal borrowed;
    private final BigDecimal withdrawing;
    private final BigDecimal depositing;


    public ExchangeBalance(String exchange, Balance balance) {
        this.exchange = exchange;
        this.currency = balance.getCurrency();
        this.total = balance.getTotal();
        this.available = balance.getAvailable();
        this.frozen = balance.getFrozen();
        this.loaned = balance.getLoaned();
        this.borrowed = balance.getBorrowed();
        this.withdrawing = balance.getWithdrawing();
        this.depositing = balance.getDepositing();
    }

    public String getExchange() {
        return exchange;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public BigDecimal getLoaned() {
        return loaned;
    }

    public BigDecimal getBorrowed() {
        return borrowed;
    }

    public BigDecimal getWithdrawing() {
        return withdrawing;
    }

    public BigDecimal getDepositing() {
        return depositing;
    }

    public String toString() {

        return "Balance [exchange=" + exchange + ", currency=" + currency + ", total=" + total + ", available=" + available + ", frozen=" + frozen + ", borrowed=" + borrowed
                + ", loaned=" + loaned + ", withdrawing=" + withdrawing + ", depositing=" + depositing + "]";
    }

}
