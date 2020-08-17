package pl.sg.accountant.transport;

import java.math.BigDecimal;
import java.util.Currency;
import javax.validation.constraints.NotNull;

public class AccountTO {
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private Currency currency;
    @NotNull
    private BigDecimal currentBalance;
    private int balanceIndex;
    @NotNull
    private String userName;

    public AccountTO() {
    }

    public AccountTO(int id, String name, Currency currency, BigDecimal currentBalance, int balanceIndex, String userName) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.currentBalance = currentBalance;
        this.balanceIndex = balanceIndex;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public AccountTO setId(int id) {
        this.id = id;
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
