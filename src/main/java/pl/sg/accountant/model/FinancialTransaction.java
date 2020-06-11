package pl.sg.accountant.model;

import pl.sg.accountant.model.validation.AccountTransaction;
import pl.sg.security.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@AccountTransaction
public class FinancialTransaction {
    @Id
    @GeneratedValue
    private int id;
    String description;
    @ManyToOne
    Account source;
    @ManyToOne
    Account destination;
    private BigDecimal debit;
    private BigDecimal credit;
    @ManyToOne
    private ApplicationUser applicationUser;


    public FinancialTransaction() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public FinancialTransaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Account getSource() {
        return source;
    }

    public FinancialTransaction setSource(Account source) {
        this.source = source;
        return this;
    }

    public Account getDestination() {
        return destination;
    }

    public FinancialTransaction setDestination(Account destination) {
        this.destination = destination;
        return this;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public FinancialTransaction setDebit(BigDecimal debit) {
        this.debit = debit;
        return this;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public FinancialTransaction setCredit(BigDecimal credit) {
        this.credit = credit;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public FinancialTransaction setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }
}