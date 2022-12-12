package pl.sg.integrations.nodrigen.model;

import org.hibernate.annotations.Immutable;
import pl.sg.accountant.model.accounts.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Immutable
@Table(name = "nodrigen_transactions_to_import")
public class NodrigenTransactionsToImport {

    @Id
    private Long id;
    private Integer domainId;
    private BigDecimal conversionRate;
    private BigDecimal credit;
    private BigDecimal debit;
    private String description;
    private Instant timeOfTransaction;
    private Integer destinationId;
    private Integer sourceId;
    @Transient
    private Account destinationAccount;
    @Transient
    private Account sourceAccount;
    private Integer creditBankAccountId;
    private Integer debitBankAccountId;
    private Integer creditNodrigenTransactionId;
    private Integer debitNodrigenTransactionId;
    private Integer nodrigenTransactionId;

    public Long getId() {
        return id;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public String getDescription() {
        return description;
    }

    public Instant getTimeOfTransaction() {
        return timeOfTransaction;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public Integer getCreditBankAccountId() {
        return creditBankAccountId;
    }

    public Integer getDebitBankAccountId() {
        return debitBankAccountId;
    }

    public Integer getCreditNodrigenTransactionId() {
        return creditNodrigenTransactionId;
    }

    public Integer getDebitNodrigenTransactionId() {
        return debitNodrigenTransactionId;
    }

    protected NodrigenTransactionsToImport() {
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public Integer getNodrigenTransactionId() {
        return nodrigenTransactionId;
    }

    public NodrigenTransactionsToImport setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
        return this;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public NodrigenTransactionsToImport setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
        return this;
    }
}