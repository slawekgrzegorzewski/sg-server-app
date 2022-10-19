package pl.sg.accountant.transport.accounts;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class PerformedService implements WithDomain {

    private Integer id;
    LocalDate date;
    Service service;
    Client client;
    BigDecimal price;
    Currency currency;
    List<PerformedServicePaymentSimpleTO> clientPaymentsRelations;
    DomainSimple domain;

    public PerformedService() {
    }

    public PerformedService(Integer id, LocalDate date, Service service, Client client, BigDecimal price, Currency currency, List<PerformedServicePaymentSimpleTO> clientPaymentsRelations, DomainSimple domain) {
        this.id = id;
        this.date = date;
        this.service = service;
        this.client = client;
        this.price = price;
        this.currency = currency;
        this.clientPaymentsRelations = clientPaymentsRelations;
        this.domain = domain;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public PerformedService setId(Integer id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public PerformedService setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Service getService() {
        return service;
    }

    public PerformedService setService(Service service) {
        this.service = service;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public PerformedService setClient(Client client) {
        this.client = client;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedService setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PerformedService setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public List<PerformedServicePaymentSimpleTO> getClientPaymentsRelations() {
        return clientPaymentsRelations;
    }

    public PerformedService setClientPaymentsRelations(List<PerformedServicePaymentSimpleTO> clientPaymentsRelations) {
        this.clientPaymentsRelations = clientPaymentsRelations;
        return this;
    }

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public PerformedService setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
