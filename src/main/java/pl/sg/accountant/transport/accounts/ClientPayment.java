package pl.sg.accountant.transport.accounts;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class ClientPayment implements WithDomain {

    private Integer id;
    private Client client;
    LocalDate date;
    BigDecimal price;
    Currency currency;
    boolean billOfSale;
    boolean billOfSaleAsInvoice;
    boolean invoice;
    boolean notRegistered;
    List<PerformedServicePaymentSimpleTO> serviceRelations;
    DomainSimple domain;

    public ClientPayment() {
    }

    public ClientPayment(Integer id, Client client, LocalDate date, BigDecimal price, Currency currency, boolean billOfSale, boolean billOfSaleAsInvoice, boolean invoice, boolean notRegistered, List<PerformedServicePaymentSimpleTO> serviceRelations, DomainSimple domain) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.price = price;
        this.currency = currency;
        this.billOfSale = billOfSale;
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        this.invoice = invoice;
        this.notRegistered = notRegistered;
        this.serviceRelations = serviceRelations;
        this.domain = domain;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ClientPayment setId(Integer id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public ClientPayment setClient(Client client) {
        this.client = client;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ClientPayment setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ClientPayment setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ClientPayment setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isBillOfSale() {
        return billOfSale;
    }

    public ClientPayment setBillOfSale(boolean billOfSale) {
        this.billOfSale = billOfSale;
        return this;
    }

    public boolean isBillOfSaleAsInvoice() {
        return billOfSaleAsInvoice;
    }

    public ClientPayment setBillOfSaleAsInvoice(boolean billOfSaleAsInvoice) {
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        return this;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public ClientPayment setInvoice(boolean invoice) {
        this.invoice = invoice;
        return this;
    }

    public boolean isNotRegistered() {
        return notRegistered;
    }

    public ClientPayment setNotRegistered(boolean notRegistered) {
        this.notRegistered = notRegistered;
        return this;
    }

    public List<PerformedServicePaymentSimpleTO> getServiceRelations() {
        return serviceRelations;
    }

    public ClientPayment setServiceRelations(List<PerformedServicePaymentSimpleTO> serviceRelations) {
        this.serviceRelations = serviceRelations;
        return this;
    }

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public ClientPayment setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
