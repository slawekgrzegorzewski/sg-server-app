package pl.sg.accountant.transport;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

import java.math.BigDecimal;
import java.util.Currency;

public class PiggyBank implements WithDomain {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal balance;
    private Currency currency;
    private boolean savings;
    private BigDecimal monthlyTopUp;
    private DomainSimple domain;

    public PiggyBank() {
    }

    public PiggyBank(int id, String name, String description, BigDecimal balance, Currency currency, boolean savings,
                     BigDecimal monthlyTopUp, DomainSimple domain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.currency = currency;
        this.savings = savings;
        this.monthlyTopUp = monthlyTopUp;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public PiggyBank setId(int id) {
        this.id = id;
        return this;
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

    public BigDecimal getMonthlyTopUp() {
        return monthlyTopUp;
    }

    public PiggyBank setMonthlyTopUp(BigDecimal monthlyTopUp) {
        this.monthlyTopUp = monthlyTopUp;
        return this;
    }

    public DomainSimple getDomain() {
        return domain;
    }

    public PiggyBank setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
