package pl.sg.integrations.nodrigen.model.balances;

import pl.sg.banks.model.BankAccount;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
public class NodrigenBankAccountBalance {
    @Id
    
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    private LocalDateTime fetchTime;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_amount_currency"))
    })
    private NodrigenAmount balanceAmount;
    private String balanceType;
    private Boolean creditLimitIncluded;
    private OffsetDateTime lastChangeDateTime;
    private String lastCommittedTransaction;
    private LocalDate referenceDate;
    @ManyToOne
    private BankAccount bankAccount;

    public Integer getId() {
        return id;
    }

    public NodrigenBankAccountBalance setId(Integer id) {
        this.id = id;
        return this;
    }

    public NodrigenAmount getBalanceAmount() {
        return balanceAmount;
    }

    public NodrigenBankAccountBalance setBalanceAmount(NodrigenAmount balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public LocalDateTime getFetchTime() {
        return fetchTime;
    }

    public NodrigenBankAccountBalance setFetchTime(LocalDateTime fetchTime) {
        this.fetchTime = fetchTime;
        return this;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public NodrigenBankAccountBalance setBalanceType(String balanceType) {
        this.balanceType = balanceType;
        return this;
    }

    public Boolean getCreditLimitIncluded() {
        return creditLimitIncluded;
    }

    public NodrigenBankAccountBalance setCreditLimitIncluded(Boolean creditLimitIncluded) {
        this.creditLimitIncluded = creditLimitIncluded;
        return this;
    }

    public OffsetDateTime getLastChangeDateTime() {
        return lastChangeDateTime;
    }

    public NodrigenBankAccountBalance setLastChangeDateTime(OffsetDateTime lastChangeDateTime) {
        this.lastChangeDateTime = lastChangeDateTime;
        return this;
    }

    public String getLastCommittedTransaction() {
        return lastCommittedTransaction;
    }

    public NodrigenBankAccountBalance setLastCommittedTransaction(String lastCommittedTransaction) {
        this.lastCommittedTransaction = lastCommittedTransaction;
        return this;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public NodrigenBankAccountBalance setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public NodrigenBankAccountBalance setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }
}
