package pl.sg.accountant.transport.accounts;

import java.math.BigDecimal;

public class PerformedServicePaymentTO {

    private Integer id;
    private PerformedServiceTO performedService;
    private ClientPaymentTO clientPayment;
    private BigDecimal price;

    public PerformedServicePaymentTO() {
    }

    public PerformedServicePaymentTO(Integer id, PerformedServiceTO performedService, ClientPaymentTO clientPayment, BigDecimal price) {
        this.id = id;
        this.performedService = performedService;
        this.clientPayment = clientPayment;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public PerformedServicePaymentTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public PerformedServiceTO getPerformedService() {
        return performedService;
    }

    public PerformedServicePaymentTO setPerformedService(PerformedServiceTO performedService) {
        this.performedService = performedService;
        return this;
    }

    public ClientPaymentTO getClientPayment() {
        return clientPayment;
    }

    public PerformedServicePaymentTO setClientPayment(ClientPaymentTO clientPayment) {
        this.clientPayment = clientPayment;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedServicePaymentTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
