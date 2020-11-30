package pl.sg.accountant.model.billings;

import pl.sg.application.model.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class PiggyBank {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String name;
    @NotNull
    @Column(length = 2000)
    private String description;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private Currency currency;
    @Column(columnDefinition = "boolean default false")
    private boolean savings;
    private BigDecimal monthlyTopUp;
    @ManyToOne
    private ApplicationUser applicationUser;

    public PiggyBank() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PiggyBank setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PiggyBank setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public PiggyBank setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PiggyBank setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isSavings() {
        return savings;
    }

    public PiggyBank setSavings(boolean savings) {
        this.savings = savings;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public PiggyBank setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public BigDecimal getMonthlyTopUp() {
        return monthlyTopUp;
    }

    public PiggyBank setMonthlyTopUp(BigDecimal monthlyTopUp) {
        this.monthlyTopUp = monthlyTopUp;
        return this;
    }

    public void addMonthlyTopUp() {
        if (this.balance != null && this.monthlyTopUp != null) {
            this.balance = this.balance.add(this.monthlyTopUp);
        }
    }
}
