package pl.sg.accountant.model.accounts;

import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.OperationType;
import pl.sg.accountant.model.validation.AccountTransaction;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@AccountTransaction
public class FinancialTransaction {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private int id;
    @Column(length = 2000)
    String description;
    @ManyToOne
    Account source;
    @ManyToOne
    Account destination;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal fee;
    private Currency feeCurrency;
    private BigDecimal otherFee;
    private Currency otherFeeCurrency;
    private BigDecimal conversionRate;
    private LocalDateTime timeOfTransaction;
    @OneToOne(optional = true, mappedBy = "creditTransaction", fetch = FetchType.LAZY)
    @Transient
    private NodrigenTransaction creditNodrigenTransaction;
    @Transient
    @OneToOne(optional = true, mappedBy = "debitTransaction", fetch = FetchType.LAZY)
    private NodrigenTransaction debitNodrigenTransaction;

    public FinancialTransaction() {
    }

    public Integer getId() {
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

    public BigDecimal getFee() {
        return fee;
    }

    public FinancialTransaction setFee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public Currency getFeeCurrency() {
        return feeCurrency;
    }

    public FinancialTransaction setFeeCurrency(Currency feeCurrency) {
        this.feeCurrency = feeCurrency;
        return this;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public FinancialTransaction setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
        return this;
    }

    public Currency getOtherFeeCurrency() {
        return otherFeeCurrency;
    }

    public FinancialTransaction setOtherFeeCurrency(Currency otherFeeCurrency) {
        this.otherFeeCurrency = otherFeeCurrency;
        return this;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public FinancialTransaction setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
        return this;
    }

    public LocalDateTime getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public FinancialTransaction setTimeOfTransaction(LocalDateTime timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
        return this;
    }

    public FinancialTransaction transfer(Account from, Account to, BigDecimal amount) {
        validateSameCurrency(from, to);
        return transfer(from, to, amount, amount, BigDecimal.ONE);
    }

    public FinancialTransaction transfer(Account from, Account to, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate) {
        validateEnoughMoney(from, amount);
        validateRate(amount, targetAmount, rate);
        this.source = from;
        this.destination = to;
        this.debit = amount;
        this.credit = targetAmount;
        this.conversionRate = rate;
        return this;
    }

    public FinancialTransaction transfer(Account account, BigDecimal amount, OperationType operationType) {
        this.conversionRate = BigDecimal.ONE;
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

    private void validateSameCurrency(Account from, Account to) {
        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new AccountsException("Accounts currencies differ: source is " + from.getCurrency().getCurrencyCode() + ", target is " + to.getCurrency().getCurrencyCode());
        }
    }

    private void validateEnoughMoney(Account account, BigDecimal amount) {
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountsException("Not enough money");
        }
    }

    private void validateRate(BigDecimal amount, BigDecimal targetAmount, BigDecimal rate) {
        BigDecimal calculation = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal roundedTarget = targetAmount.setScale(2, RoundingMode.HALF_UP);
        if (!calculation.equals(roundedTarget)) {
            throw new AccountsException("Not enough money");
        }
    }

    public NodrigenTransaction getCreditNodrigenTransaction() {
        return creditNodrigenTransaction;
    }

    public FinancialTransaction setCreditNodrigenTransaction(NodrigenTransaction creditNodrigenTransaction) {
        this.creditNodrigenTransaction = creditNodrigenTransaction;
        return this;
    }

    public NodrigenTransaction getDebitNodrigenTransaction() {
        return debitNodrigenTransaction;
    }

    public FinancialTransaction setDebitNodrigenTransaction(NodrigenTransaction debitNodrigenTransaction) {
        this.debitNodrigenTransaction = debitNodrigenTransaction;
        return this;
    }
}