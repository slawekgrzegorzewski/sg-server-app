package pl.sg.accountant.transport.billings;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class IncomeTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private CategoryTO category;
    private Date incomeDate;

    public IncomeTO() {
    }

    public IncomeTO(int id, String description, BigDecimal amount, Currency currency, CategoryTO category, Date incomeDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.incomeDate = incomeDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public CategoryTO getCategory() {
        return category;
    }

    public void setCategory(CategoryTO category) {
        this.category = category;
    }

    public Date getIncomeDate() {
        return incomeDate;
    }

    public IncomeTO setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
        return this;
    }
}
