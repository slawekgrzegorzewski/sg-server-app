package pl.sg.banks.transport;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import javax.persistence.Entity;
import java.util.Currency;

public class BankAccountTO implements WithDomainTO {
    private Integer id;
    private String iban;
    private Currency currency;
    private String owner;
    private String product;
    private String bic;
    private String externalId;
    private DomainTO domain;

    public Integer getId() {
        return id;
    }

    public BankAccountTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public BankAccountTO setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BankAccountTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public BankAccountTO setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public BankAccountTO setProduct(String product) {
        this.product = product;
        return this;
    }

    public String getBic() {
        return bic;
    }

    public BankAccountTO setBic(String bic) {
        this.bic = bic;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public BankAccountTO setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public BankAccountTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
