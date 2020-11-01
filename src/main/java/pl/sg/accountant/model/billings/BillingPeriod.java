package pl.sg.accountant.model.billings;

import pl.sg.application.model.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;

@Entity
public class BillingPeriod {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String name;
    @NotNull
    private YearMonth period;
    @OneToMany(mappedBy = "billingPeriod")
    private List<Income> incomes;
    @OneToMany(mappedBy = "billingPeriod")
    private List<Expense> expenses;
    @ManyToOne
    private ApplicationUser applicationUser;

    public BillingPeriod() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BillingPeriod setName(String name) {
        this.name = name;
        return this;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public BillingPeriod setPeriod(YearMonth period) {
        this.period = period;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public BillingPeriod setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public BillingPeriod setIncomes(List<Income> incomes) {
        this.incomes = incomes;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public BillingPeriod setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }
}
