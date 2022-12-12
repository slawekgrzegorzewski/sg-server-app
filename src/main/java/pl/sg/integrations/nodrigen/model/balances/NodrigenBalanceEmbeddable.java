package pl.sg.integrations.nodrigen.model.balances;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Embeddable
public class NodrigenBalanceEmbeddable {
    @Embedded
    private NodrigenAmount balanceAmount;
    private String balanceType;
    public Boolean creditLimitIncluded;
    public OffsetDateTime lastChangeDateTime;
    public String lastCommittedTransaction;
    private LocalDate referenceDate;

    public NodrigenAmount getBalanceAmount() {
        return balanceAmount;
    }

    public NodrigenBalanceEmbeddable setBalanceAmount(NodrigenAmount balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public NodrigenBalanceEmbeddable setBalanceType(String balanceType) {
        this.balanceType = balanceType;
        return this;
    }

    public Boolean getCreditLimitIncluded() {
        return creditLimitIncluded;
    }

    public NodrigenBalanceEmbeddable setCreditLimitIncluded(Boolean creditLimitIncluded) {
        this.creditLimitIncluded = creditLimitIncluded;
        return this;
    }

    public OffsetDateTime getLastChangeDateTime() {
        return lastChangeDateTime;
    }

    public NodrigenBalanceEmbeddable setLastChangeDateTime(OffsetDateTime lastChangeDateTime) {
        this.lastChangeDateTime = lastChangeDateTime;
        return this;
    }

    public String getLastCommittedTransaction() {
        return lastCommittedTransaction;
    }

    public NodrigenBalanceEmbeddable setLastCommittedTransaction(String lastCommittedTransaction) {
        this.lastCommittedTransaction = lastCommittedTransaction;
        return this;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public NodrigenBalanceEmbeddable setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
        return this;
    }
}
