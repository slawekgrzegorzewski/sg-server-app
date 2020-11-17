package pl.sg.accountant.transport.billings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;

public class ExpenseTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private CategoryTO category;
    private LocalDate expenseDate;

    public ExpenseTO() {
    }

    public ExpenseTO(int id, String description, BigDecimal amount, Currency currency, CategoryTO category, LocalDate expenseDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.expenseDate = expenseDate;
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

    public Currency getCurrency() {
        return currency;
    }

    public ExpenseTO setCurrency(Currency currency) {
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

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public ExpenseTO setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
        return this;
    }
}
