package pl.sg.accountant.transport;

import pl.sg.accountant.transport.accounts.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FinancialTransactionTO {
    private Integer id;
    String description;
    Account source;
    Account destination;
    private BigDecimal debit;
    private BigDecimal credit;
    private LocalDateTime timeOfTransaction;


    public FinancialTransactionTO() {
    }

    public FinancialTransactionTO(int id, String description, Account source, Account destination, BigDecimal debit, BigDecimal credit, LocalDateTime timeOfTransaction) {
        this.id = id;
        this.description = description;
        this.source = source;
        this.destination = destination;
        this.debit = debit;
        this.credit = credit;
        this.timeOfTransaction = timeOfTransaction;
    }

    public Integer getId() {
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

    public Account getSource() {
        return source;
    }

    public FinancialTransactionTO setSource(Account source) {
        this.source = source;
        return this;
    }

    public Account getDestination() {
        return destination;
    }

    public FinancialTransactionTO setDestination(Account destination) {
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