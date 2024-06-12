package pl.sg.accountant.model.accounts;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import org.javamoney.moneta.Money;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.application.database.MonetaryAmountType;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;
import pl.sg.banks.model.BankAccount;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Entity(name = "accounts")
public class Account implements WithDomain<Account> {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @NotNull
    private String name;

    @AttributeOverride(name = "amount", column = @Column(name = "current_balance", columnDefinition = "numeric(19,2) default 0"))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", updatable = false))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount currentBalance;

    @AttributeOverride(name = "amount", column = @Column(name = "credit_limit", columnDefinition = "numeric(19,2) default 0"))
    @AttributeOverride(name = "currency", column = @Column(name = "currency", insertable = false, updatable = false))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount creditLimit;

    @ManyToOne
    private FinancialTransaction lastTransactionIncludedInBalance;

    @Column(columnDefinition = "boolean not null default true")
    private boolean visible;


    @OneToOne
    BankAccount bankAccount;

    @ManyToOne
    private Domain domain;

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public Currency getCurrency() {
        return Currency.getInstance(currentBalance.getCurrency().getCurrencyCode());
    }

    public MonetaryAmount getAvailableBalance() {
        return currentBalance.add(creditLimit);
    }

    public MonetaryAmount getCurrentBalance() {
        return currentBalance;
    }

    public Account setCurrentBalance(MonetaryAmount currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public MonetaryAmount getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(MonetaryAmount creditLimit) {
        this.creditLimit = creditLimit;
    }

    public FinancialTransaction getLastTransactionIncludedInBalance() {
        return lastTransactionIncludedInBalance;
    }

    public Account setLastTransactionIncludedInBalance(FinancialTransaction lastTransactionIncludedInBalance) {
        this.lastTransactionIncludedInBalance = lastTransactionIncludedInBalance;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public Account setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public void debit(FinancialTransaction financialTransaction) {
        this.lastTransactionIncludedInBalance = financialTransaction;
        this.currentBalance = this.currentBalance.subtract(Money.of(financialTransaction.getDebit(), this.currentBalance.getCurrency().getCurrencyCode()));
    }

    public void credit(FinancialTransaction financialTransaction) {
        this.lastTransactionIncludedInBalance = financialTransaction;
        this.currentBalance = this.currentBalance.add(Money.of(financialTransaction.getCredit(), this.currentBalance.getCurrency().getCurrencyCode()));
    }

    public boolean isVisible() {
        return visible;
    }

    public Account setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Account setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }
}
