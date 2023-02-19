package pl.sg.accountant.model.accounts;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Entity
public class PerformedService implements WithDomain<PerformedService> {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;

    @NotNull
    LocalDate date;

    @ManyToOne
    @NotNull
    Service service;

    @ManyToOne
    @NotNull
    Client client;

    @NotNull
    @Positive
    @Column(precision=19, scale=6)
    BigDecimal price;

    @NotNull
    Currency currency;

    @OneToMany(mappedBy = "performedService")
    List<PerformedServicePayment> payments;

    @ManyToOne
    @NotNull
    Domain domain;

    public PerformedService() {
    }

    public PerformedService(Integer id, LocalDate date, Service service, Client client, BigDecimal price, Currency currency, List<PerformedServicePayment> payments, Domain domain) {
        this.id = id;
        this.date = date;
        this.service = service;
        this.client = client;
        this.price = price;
        this.currency = currency;
        this.payments = payments;
        this.domain = domain;
    }

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

    public List<PerformedServicePayment> getPayments() {
        return payments;
    }

    public PerformedService setPayments(List<PerformedServicePayment> payments) {
        this.payments = payments;
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public PerformedService setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
