package pl.sg.accountant.model;

import pl.sg.accountant.model.validation.AccountTransaction;
import pl.sg.accountant.service.AccountsException;
import pl.sg.application.model.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AccountTransaction
public class FinancialTransaction {
    @Id
    @GeneratedValue
    private int id;
    String description;
    @ManyToOne
    Account source;
    @ManyToOne
    Account destination;
    private BigDecimal debit;
    private BigDecimal credit;
    @ManyToOne
    private ApplicationUser applicationUser;
    private LocalDateTime timeOfTransaction;


    public FinancialTransaction() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public FinancialTransaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Account getSource() {
        return source;
    }

    public FinancialTransaction setSource(Account source) {
        this.source = source;
        return this;
    }

    public Account getDestination() {
        return destination;
    }

    public FinancialTransaction setDestination(Account destination) {
        this.destination = destination;
        return this;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public FinancialTransaction setDebit(BigDecimal debit) {
        this.debit = debit;
        return this;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public FinancialTransaction setCredit(BigDecimal credit) {
        this.credit = credit;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public FinancialTransaction setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public FinancialTransaction setTimeOfTransaction(LocalDateTime timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
        return this;
    }

    public FinancialTransaction transfer(Account from, Account to, BigDecimal amount) throws AccountsException {
        validateSameCurrency(from, to);
        validateEnoughMoney(from, amount);
        this.source = from;
        this.destination = to;
        this.credit = amount;
        this.debit = amount;
        return this;
    }

    public FinancialTransaction transfer(Account account, BigDecimal amount, OperationType operationType) throws AccountsException {
        switch (operationType) {
            case DEBIT:
                validateEnoughMoney(account, amount);
                this.source = account;
                this.destination = null;
                this.credit = BigDecimal.ZERO;
                this.debit = amount;
                break;
            case CREDIT:
                this.source = null;
                this.destination = account;
                this.credit = amount;
                this.debit = BigDecimal.ZERO;
                break;
        }
        return this;
    }

    private void validateSameCurrency(Account from, Account to) throws AccountsException {
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new AccountsException("Accounts currencies differ: source is " + from.getCurrency().getCurrencyCode() + ", target is " + to.getCurrency().getCurrencyCode());
        }
    }

    private void validateEnoughMoney(Account account, BigDecimal amount) throws AccountsException {
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountsException("Not enough money");
        }
    }
}