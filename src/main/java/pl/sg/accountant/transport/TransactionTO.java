package pl.sg.accountant.transport;

import java.math.BigDecimal;

public class TransactionTO {
    private int id;
    String description;
    AccountTO source;
    AccountTO destination;
    private BigDecimal debit;
    private BigDecimal credit;


    public TransactionTO() {
    }

    public TransactionTO(int id, String description, AccountTO source, AccountTO destination, BigDecimal debit, BigDecimal credit) {
        this.id = id;
        this.description = description;
        this.source = source;
        this.destination = destination;
        this.debit = debit;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public TransactionTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TransactionTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public AccountTO getSource() {
        return source;
    }

    public TransactionTO setSource(AccountTO source) {
        this.source = source;
        return this;
    }

    public AccountTO getDestination() {
        return destination;
    }

    public TransactionTO setDestination(AccountTO destination) {
        this.destination = destination;
        return this;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public TransactionTO setDebit(BigDecimal debit) {
        this.debit = debit;
        return this;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public TransactionTO setCredit(BigDecimal credit) {
        this.credit = credit;
        return this;
    }
}