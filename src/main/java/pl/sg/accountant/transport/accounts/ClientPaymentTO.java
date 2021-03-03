package pl.sg.accountant.transport.accounts;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class ClientPaymentTO implements WithDomainTO {

    private Integer id;
    private ClientTO client;
    LocalDate date;
    BigDecimal price;
    Currency currency;
    boolean billOfSale;
    boolean billOfSaleAsInvoice;
    boolean invoice;
    List<PerformedServicePaymentSimpleTO> serviceRelations;
    DomainTO domain;

    public ClientPaymentTO() {
    }

    public ClientPaymentTO(Integer id, ClientTO client, LocalDate date, BigDecimal price, Currency currency, boolean billOfSale, boolean billOfSaleAsInvoice, boolean invoice, List<PerformedServicePaymentSimpleTO> serviceRelations, DomainTO domain) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.price = price;
        this.currency = currency;
        this.billOfSale = billOfSale;
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        this.invoice = invoice;
        this.serviceRelations = serviceRelations;
        this.domain = domain;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public ClientPaymentTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public ClientTO getClient() {
        return client;
    }

    public ClientPaymentTO setClient(ClientTO client) {
        this.client = client;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ClientPaymentTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ClientPaymentTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ClientPaymentTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isBillOfSale() {
        return billOfSale;
    }

    public ClientPaymentTO setBillOfSale(boolean billOfSale) {
        this.billOfSale = billOfSale;
        return this;
    }

    public boolean isBillOfSaleAsInvoice() {
        return billOfSaleAsInvoice;
    }

    public ClientPaymentTO setBillOfSaleAsInvoice(boolean billOfSaleAsInvoice) {
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        return this;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public ClientPaymentTO setInvoice(boolean invoice) {
        this.invoice = invoice;
        return this;
    }

    public List<PerformedServicePaymentSimpleTO> getServiceRelations() {
        return serviceRelations;
    }

    public ClientPaymentTO setServiceRelations(List<PerformedServicePaymentSimpleTO> serviceRelations) {
        this.serviceRelations = serviceRelations;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public ClientPaymentTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
