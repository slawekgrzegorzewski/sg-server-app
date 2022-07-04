package pl.sg.integrations.nodrigen.transport;

import pl.sg.application.model.Domain;
import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
    private Integer destinationId;
    private Integer sourceId;
    private Integer creditBankAccountId;
    private Integer debitBankAccountId;
    private Integer creditNodrigenTransactionId;
    private Integer debitNodrigenTransactionId;

    public NodrigenTransactionsToImportTO() {
    }

    public NodrigenTransactionsToImportTO(Long id, Integer domainId, BigDecimal conversionRate, BigDecimal credit, BigDecimal debit, String description, Instant timeOfTransaction, Integer destinationId, Integer sourceId, Integer creditBankAccountId, Integer debitBankAccountId, Integer creditNodrigenTransactionId, Integer debitNodrigenTransactionId) {
        this.id = id;
        this.domainId = domainId;
        this.conversionRate = conversionRate;
        this.credit = credit;
        this.debit = debit;
        this.description = description;
        this.timeOfTransaction = timeOfTransaction;
        this.destinationId = destinationId;
        this.sourceId = sourceId;
        this.creditBankAccountId = creditBankAccountId;
        this.debitBankAccountId = debitBankAccountId;
        this.creditNodrigenTransactionId = creditNodrigenTransactionId;
        this.debitNodrigenTransactionId = debitNodrigenTransactionId;
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

    public Integer getDestinationId() {
        return destinationId;
    }

    public NodrigenTransactionsToImportTO setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
        return this;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public NodrigenTransactionsToImportTO setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
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
}