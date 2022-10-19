package pl.sg.accountant.transport.accounts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PerformedServicePaymentTO {

    private Integer id;
    private PerformedService performedService;
    private ClientPayment clientPayment;
    private LocalDate date;
    private BigDecimal price;
    Currency currency;
    boolean billOfSale;
    boolean billOfSaleAsInvoice;
    boolean invoice;
    boolean notRegistered;

    public PerformedServicePaymentTO() {
    }

    public PerformedServicePaymentTO(Integer id, PerformedService performedService, ClientPayment clientPayment, LocalDate date, BigDecimal price, Currency currency, boolean billOfSale, boolean billOfSaleAsInvoice, boolean invoice, boolean notRegistered) {
        this.id = id;
        this.performedService = performedService;
        this.clientPayment = clientPayment;
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

    public PerformedServicePaymentTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public PerformedService getPerformedService() {
        return performedService;
    }

    public PerformedServicePaymentTO setPerformedService(PerformedService performedService) {
        this.performedService = performedService;
        return this;
    }

    public ClientPayment getClientPayment() {
        return clientPayment;
    }

    public PerformedServicePaymentTO setClientPayment(ClientPayment clientPayment) {
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

    public LocalDate getDate() {
        return date;
    }

    public PerformedServicePaymentTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PerformedServicePaymentTO setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public boolean isBillOfSale() {
        return billOfSale;
    }

    public PerformedServicePaymentTO setBillOfSale(Boolean billOfSale) {
        this.billOfSale = billOfSale;
        return this;
    }

    public boolean isBillOfSaleAsInvoice() {
        return billOfSaleAsInvoice;
    }

    public PerformedServicePaymentTO setBillOfSaleAsInvoice(Boolean billOfSaleAsInvoice) {
        this.billOfSaleAsInvoice = billOfSaleAsInvoice;
        return this;
    }

    public boolean isInvoice() {
        return invoice;
    }

    public PerformedServicePaymentTO setInvoice(Boolean invoice) {
        this.invoice = invoice;
        return this;
    }

    public boolean isNotRegistered() {
        return notRegistered;
    }

    public PerformedServicePaymentTO setNotRegistered(Boolean notRegistered) {
        this.notRegistered = notRegistered;
        return this;
    }
}
