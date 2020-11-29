package pl.sg.accountant.transport.accounts;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Currency;

public class AccountTO {
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
    @NotNull
    private int userId;

    public AccountTO() {
    }

    public AccountTO(int id, String name, Currency currency, BigDecimal currentBalance, int balanceIndex, int userId) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.currentBalance = currentBalance;
        this.balanceIndex = balanceIndex;
        this.userId = userId;
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

    public Integer getUserId() {
        return userId;
    }

    public AccountTO setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
}
