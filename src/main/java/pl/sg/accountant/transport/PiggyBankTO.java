package pl.sg.accountant.transport;

import java.math.BigDecimal;
import java.util.Currency;

public class PiggyBankTO {
    private int id;
    private String name;
    private String description;
    private BigDecimal balance;
    private Currency currency;
    private String userName;

    public PiggyBankTO() {
    }

    public PiggyBankTO(int id, String name, String description, BigDecimal balance, Currency currency, String userName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.currency = currency;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public PiggyBankTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
