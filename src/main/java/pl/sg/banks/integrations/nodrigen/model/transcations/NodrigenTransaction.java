package pl.sg.banks.integrations.nodrigen.model.transcations;

import pl.sg.banks.integrations.nodrigen.model.balances.NodrigenAmount;
import pl.sg.banks.integrations.nodrigen.model.balances.NodrigenBalanceEmbeddable;
import pl.sg.banks.model.BankAccount;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
public class NodrigenTransaction {
    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private NodrigenPhase phase;
    private String additionalInformation;
    private String additionalInformationStructured;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balanceAmount.amount", column = @Column(name = "balance_after_transaction_amount")),
            @AttributeOverride(name = "balanceAmount.currency", column = @Column(name = "balance_after_transaction_currency")),
            @AttributeOverride(name = "balanceType", column = @Column(name = "balance_after_transaction_balance_type")),
            @AttributeOverride(name = "creditLimitIncluded", column = @Column(name = "balance_after_transaction_credit_limit_included")),
            @AttributeOverride(name = "lastChangeDateTime", column = @Column(name = "balance_after_transaction_last_change_date_time")),
            @AttributeOverride(name = "lastCommittedTransaction", column = @Column(name = "balance_after_transaction_last_committed_transaction")),
            @AttributeOverride(name = "referenceDate", column = @Column(name = "balance_after_transaction_reference_date"))
    })
    private NodrigenBalanceEmbeddable balanceAfterTransaction;
    private String bankTransactionCode;
    private LocalDate bookingDate;
    private OffsetDateTime bookingDateTime;
    private String checkId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bban", column = @Column(name = "creditor_account_bban")),
            @AttributeOverride(name = "iban", column = @Column(name = "creditor_account_iban"))
    })
    private NodrigenAccount creditorAccount;
    private String creditorAgent;
    private String creditorId;
    private String creditorName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "exchangeRate", column = @Column(name = "currency_exchange_rate")),
            @AttributeOverride(name = "sourceCurrency", column = @Column(name = "currency_exchange_source_currency")),
            @AttributeOverride(name = "targetCurrency", column = @Column(name = "currency_exchange_target_currency")),
            @AttributeOverride(name = "unitCurrency", column = @Column(name = "currency_exchange_unit_currency")),
            @AttributeOverride(name = "instructedAmount.currency", column = @Column(name = "currency_exchange_instructed_amount_currency")),
            @AttributeOverride(name = "instructedAmount.amount", column = @Column(name = "currency_exchange_instructed_amount_amount"))
    })
    private NodrigenCurrencyExchange currencyExchange;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bban", column = @Column(name = "debtor_account_bban")),
            @AttributeOverride(name = "iban", column = @Column(name = "debtor_account_iban"))
    })
    private NodrigenAccount debtorAccount;
    private String debtorAgent;
    private String debtorName;
    private String entryReference;
    private String mandateId;
    private String purposeCode;
    private String proprietaryBankTransactionCode;
    private String remittanceInformationStructured;
    private String remittanceInformationStructuredArray;
    private String remittanceInformationUnstructured;
    private String remittanceInformationUnstructuredArray;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "transaction_amount_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "transaction_amount_currency"))
    })
    private NodrigenAmount transactionAmount;
    private String transactionId;
    private String ultimateCreditor;
    private String ultimateDebtor;
    private LocalDate valueDate;
    private OffsetDateTime valueDateTime;
    @ManyToOne
    private BankAccount bankAccount;

    public Integer getId() {
        return id;
    }

    public NodrigenTransaction setId(Integer id) {
        this.id = id;
        return this;
    }

    public NodrigenPhase getPhase() {
        return phase;
    }

    public NodrigenTransaction setPhase(NodrigenPhase phase) {
        this.phase = phase;
        return this;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public NodrigenTransaction setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public String getAdditionalInformationStructured() {
        return additionalInformationStructured;
    }

    public NodrigenTransaction setAdditionalInformationStructured(String additionalInformationStructured) {
        this.additionalInformationStructured = additionalInformationStructured;
        return this;
    }

    public NodrigenBalanceEmbeddable getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public NodrigenTransaction setBalanceAfterTransaction(NodrigenBalanceEmbeddable balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
        return this;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public NodrigenTransaction setBankTransactionCode(String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
        return this;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public NodrigenTransaction setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
        return this;
    }

    public OffsetDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public NodrigenTransaction setBookingDateTime(OffsetDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
        return this;
    }

    public String getCheckId() {
        return checkId;
    }

    public NodrigenTransaction setCheckId(String checkId) {
        this.checkId = checkId;
        return this;
    }

    public NodrigenAccount getCreditorAccount() {
        return creditorAccount;
    }

    public NodrigenTransaction setCreditorAccount(NodrigenAccount creditorAccount) {
        this.creditorAccount = creditorAccount;
        return this;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public NodrigenTransaction setCreditorAgent(String creditorAgent) {
        this.creditorAgent = creditorAgent;
        return this;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public NodrigenTransaction setCreditorId(String creditorId) {
        this.creditorId = creditorId;
        return this;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public NodrigenTransaction setCreditorName(String creditorName) {
        this.creditorName = creditorName;
        return this;
    }

    public NodrigenCurrencyExchange getCurrencyExchange() {
        return currencyExchange;
    }

    public NodrigenTransaction setCurrencyExchange(NodrigenCurrencyExchange currencyExchange) {
        this.currencyExchange = currencyExchange;
        return this;
    }

    public NodrigenAccount getDebtorAccount() {
        return debtorAccount;
    }

    public NodrigenTransaction setDebtorAccount(NodrigenAccount debtorAccount) {
        this.debtorAccount = debtorAccount;
        return this;
    }

    public String getDebtorAgent() {
        return debtorAgent;
    }

    public NodrigenTransaction setDebtorAgent(String debtorAgent) {
        this.debtorAgent = debtorAgent;
        return this;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public NodrigenTransaction setDebtorName(String debtorName) {
        this.debtorName = debtorName;
        return this;
    }

    public String getEntryReference() {
        return entryReference;
    }

    public NodrigenTransaction setEntryReference(String entryReference) {
        this.entryReference = entryReference;
        return this;
    }

    public String getMandateId() {
        return mandateId;
    }

    public NodrigenTransaction setMandateId(String mandateId) {
        this.mandateId = mandateId;
        return this;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public NodrigenTransaction setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
        return this;
    }

    public String getProprietaryBankTransactionCode() {
        return proprietaryBankTransactionCode;
    }

    public NodrigenTransaction setProprietaryBankTransactionCode(String proprietaryBankTransactionCode) {
        this.proprietaryBankTransactionCode = proprietaryBankTransactionCode;
        return this;
    }

    public String getRemittanceInformationStructured() {
        return remittanceInformationStructured;
    }

    public NodrigenTransaction setRemittanceInformationStructured(String remittanceInformationStructured) {
        this.remittanceInformationStructured = remittanceInformationStructured;
        return this;
    }

    public String getRemittanceInformationStructuredArray() {
        return remittanceInformationStructuredArray;
    }

    public NodrigenTransaction setRemittanceInformationStructuredArray(String remittanceInformationStructuredArray) {
        this.remittanceInformationStructuredArray = remittanceInformationStructuredArray;
        return this;
    }

    public String getRemittanceInformationUnstructured() {
        return remittanceInformationUnstructured;
    }

    public NodrigenTransaction setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
        this.remittanceInformationUnstructured = remittanceInformationUnstructured;
        return this;
    }

    public String getRemittanceInformationUnstructuredArray() {
        return remittanceInformationUnstructuredArray;
    }

    public NodrigenTransaction setRemittanceInformationUnstructuredArray(String remittanceInformationUnstructuredArray) {
        this.remittanceInformationUnstructuredArray = remittanceInformationUnstructuredArray;
        return this;
    }

    public NodrigenAmount getTransactionAmount() {
        return transactionAmount;
    }

    public NodrigenTransaction setTransactionAmount(NodrigenAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public NodrigenTransaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getUltimateCreditor() {
        return ultimateCreditor;
    }

    public NodrigenTransaction setUltimateCreditor(String ultimateCreditor) {
        this.ultimateCreditor = ultimateCreditor;
        return this;
    }

    public String getUltimateDebtor() {
        return ultimateDebtor;
    }

    public NodrigenTransaction setUltimateDebtor(String ultimateDebtor) {
        this.ultimateDebtor = ultimateDebtor;
        return this;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public NodrigenTransaction setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public OffsetDateTime getValueDateTime() {
        return valueDateTime;
    }

    public NodrigenTransaction setValueDateTime(OffsetDateTime valueDateTime) {
        this.valueDateTime = valueDateTime;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public NodrigenTransaction setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }
}
