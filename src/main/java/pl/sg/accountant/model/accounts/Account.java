package pl.sg.accountant.model.accounts;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Account implements WithDomain<Account> {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Currency currency;
    @Column(columnDefinition = "numeric(19,2) default 0")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    @PositiveOrZero
    @NotNull
    private BigDecimal currentBalance = new BigDecimal(0);
    @ManyToOne
    private FinancialTransaction lastTransactionIncludedInBalance;
    @ManyToOne
    private Domain domain;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Domain getDomain() {
        return domain;
    }

    public Account setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public void debit(FinancialTransaction financialTransaction) {
        this.lastTransactionIncludedInBalance = financialTransaction;
        this.currentBalance = this.currentBalance.subtract(financialTransaction.getDebit());
    }

    public void credit(FinancialTransaction financialTransaction) {
        this.lastTransactionIncludedInBalance = financialTransaction;
        this.currentBalance = this.currentBalance.add(financialTransaction.getCredit());
    }
}
