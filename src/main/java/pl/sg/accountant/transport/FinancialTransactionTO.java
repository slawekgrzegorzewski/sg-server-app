package pl.sg.accountant.transport;

import pl.sg.accountant.transport.accounts.AccountTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialTransactionTO {
    private int id;
    String description;
    AccountTO source;
    AccountTO destination;
    private BigDecimal debit;
    private BigDecimal credit;
    private LocalDateTime timeOfTransaction;


    public FinancialTransactionTO() {
    }

    public FinancialTransactionTO(int id, String description, AccountTO source, AccountTO destination, BigDecimal debit, BigDecimal credit, LocalDateTime timeOfTransaction) {
        this.id = id;
        this.description = description;
        this.source = source;
        this.destination = destination;
        this.debit = debit;
        this.credit = credit;
        this.timeOfTransaction = timeOfTransaction;
    }

    public int getId() {
        return id;
    }

    public FinancialTransactionTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FinancialTransactionTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public AccountTO getSource() {
        return source;
    }

    public FinancialTransactionTO setSource(AccountTO source) {
        this.source = source;
        return this;
    }

    public AccountTO getDestination() {
        return destination;
    }

    public FinancialTransactionTO setDestination(AccountTO destination) {
        this.destination = destination;
        return this;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public FinancialTransactionTO setDebit(BigDecimal debit) {
        this.debit = debit;
        return this;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public FinancialTransactionTO setCredit(BigDecimal credit) {
        this.credit = credit;
        return this;
    }

    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public FinancialTransactionTO setTimeOfTransaction(LocalDateTime timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
        return this;
    }
}