package pl.sg.accountant.transport.accounts;

import java.math.BigDecimal;

public class PerformedServicePaymentSimpleTO {

    private Integer id;
    private Integer performedServiceId;
    private Integer clientPaymentId;
    private BigDecimal price;

    public PerformedServicePaymentSimpleTO() {
    }

    public PerformedServicePaymentSimpleTO(Integer id, Integer performedServiceId, Integer clientPaymentId, BigDecimal price) {
        this.id = id;
        this.performedServiceId = performedServiceId;
        this.clientPaymentId = clientPaymentId;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public PerformedServicePaymentSimpleTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPerformedServiceId() {
        return performedServiceId;
    }

    public PerformedServicePaymentSimpleTO setPerformedServiceId(Integer performedServiceId) {
        this.performedServiceId = performedServiceId;
        return this;
    }

    public Integer getClientPaymentId() {
        return clientPaymentId;
    }

    public PerformedServicePaymentSimpleTO setClientPaymentId(Integer clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedServicePaymentSimpleTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
