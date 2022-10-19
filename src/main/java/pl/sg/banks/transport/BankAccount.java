package pl.sg.banks.transport;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

import java.util.Currency;

public class BankAccount implements WithDomain {
    private Integer id;
    private String iban;
    private Currency currency;
    private String owner;
    private String product;
    private String bic;
    private String externalId;
    private DomainSimple domain;

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

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public BankAccount setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
