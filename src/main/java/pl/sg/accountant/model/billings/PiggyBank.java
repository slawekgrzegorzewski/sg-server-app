package pl.sg.accountant.model.billings;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class PiggyBank implements WithDomain<PiggyBank> {
    @Id
    
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
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
    private Domain domain;

    public PiggyBank() {
    }

    public Integer getId() {
        return id;
    }

    public PiggyBank setId(Integer id) {
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

    public Domain getDomain() {
        return domain;
    }

    public PiggyBank setDomain(Domain domain) {
        this.domain = domain;
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
