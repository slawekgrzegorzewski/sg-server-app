package pl.sg.accountant.transport.billings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class IncomeTO {
    private Integer id;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private CategoryTO category;
    private LocalDate incomeDate;

    public IncomeTO() {
    }

    public IncomeTO(int id, String description, BigDecimal amount, Currency currency, CategoryTO category, LocalDate incomeDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.incomeDate = incomeDate;
    }

    public Integer getId() {
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

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public IncomeTO setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
        return this;
    }
}
