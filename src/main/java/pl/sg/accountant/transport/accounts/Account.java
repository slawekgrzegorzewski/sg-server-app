package pl.sg.accountant.transport.accounts;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;
import pl.sg.banks.transport.BankAccount;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Currency;

public class Account implements WithDomain {
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private Currency currency;
    @NotNull
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    @PositiveOrZero
    private BigDecimal currentBalance;
    private int balanceIndex;
    private boolean visible;
    private BankAccount bankAccount;
    private DomainSimple domain;

    public Account() {
    }

    public Account(int id, String name, Currency currency, BigDecimal currentBalance, int balanceIndex, boolean visible, BankAccount bankAccount, DomainSimple domain) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.currentBalance = currentBalance;
        this.balanceIndex = balanceIndex;
        this.visible = visible;
        this.bankAccount = bankAccount;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public Account setId(int id) {
        this.id = id;
        return this;
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

    public int getBalanceIndex() {
        return balanceIndex;
    }

    public Account setBalanceIndex(int balanceIndex) {
        this.balanceIndex = balanceIndex;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Account setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public DomainSimple getDomain() {
        return domain;
    }

    public Account setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public Account setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}