package pl.sg.accountant.model;

import pl.sg.security.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private Currency currency;
    private BigDecimal currentBalance;
    private int balanceIndex;
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

    public int getBalanceIndex() {
        return balanceIndex;
    }

    public Account setBalanceIndex(int balanceIndex) {
        this.balanceIndex = balanceIndex;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
