package pl.sg.accountant.model.billings;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Income {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private BillingPeriod billingPeriod;
    @NotNull
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Category category;

    public Income() {
    }

    public int getId() {
        return id;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
