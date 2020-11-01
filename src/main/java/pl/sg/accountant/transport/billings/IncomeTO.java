package pl.sg.accountant.transport.billings;

import pl.sg.accountant.transport.CurrencyTO;

import java.math.BigDecimal;

public class IncomeTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private CurrencyTO currency;
    private CategoryTO category;

    public IncomeTO() {
    }

    public IncomeTO(int id, String description, BigDecimal amount, CurrencyTO currency, CategoryTO category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
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

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public CategoryTO getCategory() {
        return category;
    }

    public void setCategory(CategoryTO category) {
        this.category = category;
    }
}
