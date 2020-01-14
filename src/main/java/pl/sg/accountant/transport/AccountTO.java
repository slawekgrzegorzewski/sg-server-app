package pl.sg.accountant.transport;

import java.math.BigDecimal;
import java.util.Currency;

public class AccountTO {
    private Integer id;
    private String name;
    private Currency currency;
    private BigDecimal currentBalance;
    private int balanceIndex;

    public AccountTO() {
    }

    public AccountTO(int id, String name, Currency currency, BigDecimal currentBalance, int balanceIndex) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.currentBalance = currentBalance;
        this.balanceIndex = balanceIndex;
    }

    public int getId() {
        return id;
    }

    public AccountTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountTO setName(String name) {
        this.name = name;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public AccountTO setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public int getBalanceIndex() {
        return balanceIndex;
    }

    public AccountTO setBalanceIndex(int balanceIndex) {
        this.balanceIndex = balanceIndex;
        return this;
    }
}
