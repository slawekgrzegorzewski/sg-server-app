package pl.sg.accountant.model.accounts;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class PerformedServicePayment {

    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;

    @ManyToOne
    @NotNull
    private PerformedService performedService;

    @ManyToOne
    @NotNull
    private ClientPayment clientPayment;

    @NotNull
    @Positive
    private BigDecimal price;

    public PerformedServicePayment() {
    }

    public PerformedServicePayment(Integer id, @NotNull PerformedService performedService, @NotNull ClientPayment clientPayment, @NotNull @Positive BigDecimal price) {
        this.id = id;
        this.performedService = performedService;
        this.clientPayment = clientPayment;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public PerformedServicePayment setId(Integer id) {
        this.id = id;
        return this;
    }

    public PerformedService getPerformedService() {
        return performedService;
    }

    public PerformedServicePayment setPerformedService(PerformedService performedService) {
        this.performedService = performedService;
        return this;
    }

    public ClientPayment getClientPayment() {
        return clientPayment;
    }

    public PerformedServicePayment setClientPayment(ClientPayment clientPayment) {
        this.clientPayment = clientPayment;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedServicePayment setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
