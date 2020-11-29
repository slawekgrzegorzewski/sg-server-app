package pl.sg.accountant.transport;

import java.math.BigDecimal;
import java.util.Currency;

public class PiggyBankTO {
    private int id;
    private String name;
    private String description;
    private BigDecimal balance;
    private Currency currency;
    private boolean savings;
    private BigDecimal monthlyTopUp;
    private int userId;

    public PiggyBankTO() {
    }

    public PiggyBankTO(int id, String name, String description, BigDecimal balance, Currency currency, boolean savings,
                       BigDecimal monthlyTopUp, int userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.currency = currency;
        this.savings = savings;
        this.monthlyTopUp = monthlyTopUp;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public PiggyBankTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PiggyBankTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PiggyBankTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public PiggyBankTO setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PiggyBankTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isSavings() {
        return savings;
    }

    public PiggyBankTO setSavings(boolean savings) {
        this.savings = savings;
        return this;
    }

    public BigDecimal getMonthlyTopUp() {
        return monthlyTopUp;
    }

    public PiggyBankTO setMonthlyTopUp(BigDecimal monthlyTopUp) {
        this.monthlyTopUp = monthlyTopUp;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public PiggyBankTO setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
}
