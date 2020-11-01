package pl.sg.accountant.transport.billings;

import pl.sg.accountant.transport.CurrencyTO;

import java.math.BigDecimal;

public class ExpenseTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private CurrencyTO currency;
    private CategoryTO category;

    public ExpenseTO() {
    }

    public ExpenseTO(int id, String description, BigDecimal amount, CurrencyTO currency, CategoryTO category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public ExpenseTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ExpenseTO setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public ExpenseTO setCurrency(CurrencyTO currency) {
        this.currency = currency;
        return this;
    }

    public CategoryTO getCategory() {
        return category;
    }

    public ExpenseTO setCategory(CategoryTO category) {
        this.category = category;
        return this;
    }
}
