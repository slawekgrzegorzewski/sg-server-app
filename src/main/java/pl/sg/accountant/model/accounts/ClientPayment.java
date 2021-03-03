package pl.sg.accountant.model.accounts;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Entity
public class ClientPayment implements WithDomain<ClientPayment> {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    LocalDate date;

    @ManyToOne
    @NotNull
    Client client;

    @NotNull @Positive
    BigDecimal price;

    @NotNull
    Currency currency;

    @NotNull
    boolean billOfSale;

    @NotNull
    boolean billOfSaleAsInvoice;

    @NotNull
    boolean invoice;

    @ManyToOne
    @NotNull
    Domain domain;

    @OneToMany(mappedBy = "clientPayment")
    List<PerformedServicePayment> services;

    public ClientPayment() {
    }

    public ClientPayment(Integer id, @NotNull LocalDate date, @NotNull Client client, @NotNull @Positive BigDecimal price, @NotNull Currency currency, @NotNull boolean billOfSale, @NotNull boolean billOfSaleAsInvoice, @NotNull boolean invoice, @NotNull Domain domain, List<PerformedServicePayment> services) {
        this.id = id;
        this.date = date;
        this.client = client;
        this.price = price;
        this.currency = currency;
        this.billOfSale = billOfSale;
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        this.invoice = invoice;
        this.domain = domain;
        this.services = services;
    }

    public Integer getId() {
        return id;
    }

    public ClientPayment setId(Integer id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ClientPayment setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public ClientPayment setClient(Client client) {
        this.client = client;
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

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public ClientPayment setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public List<PerformedServicePayment> getServices() {
        return services;
    }

    public ClientPayment setServices(List<PerformedServicePayment> services) {
        this.services = services;
        return this;
    }
}
