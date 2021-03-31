package pl.sg.accountant.transport.accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PerformedServicePaymentSimpleTO {

    private Integer id;
    private Integer performedServiceId;
    private Integer clientPaymentId;
    private LocalDate date;
    private BigDecimal price;
    Currency currency;
    boolean billOfSale;
    boolean billOfSaleAsInvoice;
    boolean invoice;
    boolean notRegistered;

    public PerformedServicePaymentSimpleTO() {
    }

    public PerformedServicePaymentSimpleTO(Integer id, Integer performedServiceId, Integer clientPaymentId, LocalDate date, BigDecimal price, Currency currency, boolean billOfSale, boolean billOfSaleAsInvoice, boolean invoice, boolean notRegistered) {
        this.id = id;
        this.performedServiceId = performedServiceId;
        this.clientPaymentId = clientPaymentId;
        this.date = date;
        this.price = price;
        this.currency = currency;
        this.billOfSale = billOfSale;
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        this.invoice = invoice;
        this.notRegistered = notRegistered;
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

    public LocalDate getDate() {
        return date;
    }

    public PerformedServicePaymentSimpleTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PerformedServicePaymentSimpleTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PerformedServicePaymentSimpleTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isBillOfSale() {
        return billOfSale;
    }

    public PerformedServicePaymentSimpleTO setBillOfSale(Boolean billOfSale) {
        this.billOfSale = billOfSale;
        return this;
    }

    public boolean isBillOfSaleAsInvoice() {
        return billOfSaleAsInvoice;
    }

    public PerformedServicePaymentSimpleTO setBillOfSaleAsInvoice(Boolean billOfSaleAsInvoice) {
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        return this;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public PerformedServicePaymentSimpleTO setInvoice(Boolean invoice) {
        this.invoice = invoice;
        return this;
    }

    public boolean isNotRegistered() {
        return notRegistered;
    }

    public PerformedServicePaymentSimpleTO setNotRegistered(Boolean notRegistered) {
        this.notRegistered = notRegistered;
        return this;
    }
}
