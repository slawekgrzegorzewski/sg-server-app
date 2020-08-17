package pl.sg.accountant.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import pl.sg.application.model.ApplicationUser;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String name;
    @NotNull
    private Currency currency;
    @Column(columnDefinition = "numeric(19,2) default 0")
    @NotNull
    private BigDecimal currentBalance = new BigDecimal(0);
    @ManyToOne
    private FinancialTransaction lastTransactionIncludedInBalance;
    @ManyToOne
    private ApplicationUser applicationUser;

    public Account() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Account setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public Account setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public FinancialTransaction getLastTransactionIncludedInBalance() {
        return lastTransactionIncludedInBalance;
    }

    public Account setLastTransactionIncludedInBalance(FinancialTransaction lastTransactionIncludedInBalance) {
        this.lastTransactionIncludedInBalance = lastTransactionIncludedInBalance;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
