package pl.sg.banks.model;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;
import pl.sg.integrations.nodrigen.model.balances.NodrigenBankAccountBalance;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Currency;
import java.util.List;

@Entity
public class BankAccount implements WithDomain<BankAccount> {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String iban;
    @NotNull
    private Currency currency;
    @NotNull
    private String owner;
    private String product;
    private String bic;
    @NotNull
    private String externalId;
    @ManyToOne
    private BankPermission bankPermission;
    @OneToOne(mappedBy = "bankAccount")
    Account account;
    @ManyToOne
    private Domain domain;
    @OneToMany(mappedBy = "bankAccount")
    private List<NodrigenTransaction> nodrigenTransactions;
    @OneToMany(mappedBy = "bankAccount")
    private List<NodrigenBankAccountBalance> nodrigenBalances;

    public Integer getId() {
        return id;
    }

    public BankAccount setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public BankAccount setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BankAccount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public BankAccount setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public BankAccount setProduct(String product) {
        this.product = product;
        return this;
    }

    public String getBic() {
        return bic;
    }

    public BankAccount setBic(String bic) {
        this.bic = bic;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public BankAccount setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public BankAccount setAccount(Account account) {
        this.account = account;
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public BankAccount setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public BankPermission getBankPermission() {
        return bankPermission;
    }

    public BankAccount setBankPermission(BankPermission bankPermission) {
        this.bankPermission = bankPermission;
        return this;
    }

    public List<NodrigenTransaction> getNodrigenTransactions() {
        return nodrigenTransactions;
    }

    public BankAccount setNodrigenTransactions(List<NodrigenTransaction> nodrigenTransactions) {
        this.nodrigenTransactions = nodrigenTransactions;
        return this;
    }

    public List<NodrigenBankAccountBalance> getNodrigenBalances() {
        return nodrigenBalances;
    }

    public BankAccount setNodrigenBalances(List<NodrigenBankAccountBalance> nodrigenBalances) {
        this.nodrigenBalances = nodrigenBalances;
        return this;
    }
}
