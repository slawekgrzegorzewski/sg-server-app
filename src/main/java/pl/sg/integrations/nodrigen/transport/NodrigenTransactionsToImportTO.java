package pl.sg.integrations.nodrigen.transport;

import pl.sg.accountant.transport.accounts.AccountTO;

import java.math.BigDecimal;
import java.time.Instant;

public class NodrigenTransactionsToImportTO {
    private Long id;
    private Integer domainId;
    private BigDecimal conversionRate;
    private BigDecimal credit;
    private BigDecimal debit;
    private String description;
    private Instant timeOfTransaction;
    private AccountTO destinationAccount;
    private AccountTO sourceAccount;
    private Integer creditBankAccountId;
    private Integer debitBankAccountId;
    private Integer creditNodrigenTransactionId;
    private Integer debitNodrigenTransactionId;
    private Integer nodrigenTransactionId;

    public NodrigenTransactionsToImportTO() {
    }

    public NodrigenTransactionsToImportTO(Long id, Integer domainId, BigDecimal conversionRate, BigDecimal credit, BigDecimal debit, String description, Instant timeOfTransaction, AccountTO destinationAccount, AccountTO sourceAccount, Integer creditBankAccountId, Integer debitBankAccountId, Integer creditNodrigenTransactionId, Integer debitNodrigenTransactionId, Integer nodrigenTransactionId) {
        this.id = id;
        this.domainId = domainId;
        this.conversionRate = conversionRate;
        this.credit = credit;
        this.debit = debit;
        this.description = description;
        this.timeOfTransaction = timeOfTransaction;
        this.destinationAccount = destinationAccount;
        this.sourceAccount = sourceAccount;
        this.creditBankAccountId = creditBankAccountId;
        this.debitBankAccountId = debitBankAccountId;
        this.creditNodrigenTransactionId = creditNodrigenTransactionId;
        this.debitNodrigenTransactionId = debitNodrigenTransactionId;
        this.nodrigenTransactionId = nodrigenTransactionId;
    }

    public Long getId() {
        return id;
    }

    public NodrigenTransactionsToImportTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public NodrigenTransactionsToImportTO setDomain(Integer domainId) {
        this.domainId = domainId;
        return this;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public NodrigenTransactionsToImportTO setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
        return this;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public NodrigenTransactionsToImportTO setCredit(BigDecimal credit) {
        this.credit = credit;
        return this;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public NodrigenTransactionsToImportTO setDebit(BigDecimal debit) {
        this.debit = debit;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NodrigenTransactionsToImportTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Instant getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public NodrigenTransactionsToImportTO setTimeOfTransaction(Instant timeOfTransaction) {
        this.timeOfTransaction = timeOfTransaction;
        return this;
    }

    public AccountTO getDestinationAccount() {
        return destinationAccount;
    }

    public NodrigenTransactionsToImportTO setDestinationAccount(AccountTO destinationAccount) {
        this.destinationAccount = destinationAccount;
        return this;
    }

    public AccountTO getSourceAccount() {
        return sourceAccount;
    }

    public NodrigenTransactionsToImportTO setSourceAccount(AccountTO sourceAccount) {
        this.sourceAccount = sourceAccount;
        return this;
    }

    public Integer getCreditBankAccountId() {
        return creditBankAccountId;
    }

    public NodrigenTransactionsToImportTO setCreditBankAccountId(Integer creditBankAccountId) {
        this.creditBankAccountId = creditBankAccountId;
        return this;
    }

    public Integer getDebitBankAccountId() {
        return debitBankAccountId;
    }

    public NodrigenTransactionsToImportTO setDebitBankAccountId(Integer debitBankAccountId) {
        this.debitBankAccountId = debitBankAccountId;
        return this;
    }

    public Integer getCreditNodrigenTransactionId() {
        return creditNodrigenTransactionId;
    }

    public NodrigenTransactionsToImportTO setCreditNodrigenTransactionId(Integer creditNodrigenTransactionId) {
        this.creditNodrigenTransactionId = creditNodrigenTransactionId;
        return this;
    }

    public Integer getDebitNodrigenTransactionId() {
        return debitNodrigenTransactionId;
    }

    public NodrigenTransactionsToImportTO setDebitNodrigenTransactionId(Integer debitNodrigenTransactionId) {
        this.debitNodrigenTransactionId = debitNodrigenTransactionId;
        return this;
    }

    public Integer getNodrigenTransactionId() {
        return nodrigenTransactionId;
    }

    public NodrigenTransactionsToImportTO setNodrigenTransactionId(Integer nodrigenTransactionId) {
        this.nodrigenTransactionId = nodrigenTransactionId;
        return this;
    }
}