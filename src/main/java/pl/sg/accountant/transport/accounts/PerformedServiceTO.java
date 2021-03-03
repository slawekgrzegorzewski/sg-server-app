package pl.sg.accountant.transport.accounts;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class PerformedServiceTO implements WithDomainTO {

    private Integer id;
    LocalDate date;
    ServiceTO service;
    ClientTO client;
    BigDecimal price;
    Currency currency;
    List<PerformedServicePaymentSimpleTO> clientPaymentsRelations;
    DomainTO domain;

    public PerformedServiceTO() {
    }

    public PerformedServiceTO(Integer id, LocalDate date, ServiceTO service, ClientTO client, BigDecimal price, Currency currency, List<PerformedServicePaymentSimpleTO> clientPaymentsRelations, DomainTO domain) {
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

    public PerformedServiceTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public PerformedServiceTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public ServiceTO getService() {
        return service;
    }

    public PerformedServiceTO setService(ServiceTO service) {
        this.service = service;
        return this;
    }

    public ClientTO getClient() {
        return client;
    }

    public PerformedServiceTO setClient(ClientTO client) {
        this.client = client;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedServiceTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PerformedServiceTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public List<PerformedServicePaymentSimpleTO> getClientPaymentsRelations() {
        return clientPaymentsRelations;
    }

    public PerformedServiceTO setClientPaymentsRelations(List<PerformedServicePaymentSimpleTO> clientPaymentsRelations) {
        this.clientPaymentsRelations = clientPaymentsRelations;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public PerformedServiceTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
