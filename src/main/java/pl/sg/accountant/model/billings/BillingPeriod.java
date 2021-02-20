package pl.sg.accountant.model.billings;

import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.application.database.YearMonthStringAttributeConverter;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;

@Entity
public class BillingPeriod implements WithDomain<BillingPeriod> {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Convert(converter = YearMonthStringAttributeConverter.class)
    private YearMonth period;
    @OneToMany(mappedBy = "billingPeriod")
    private List<Income> incomes;
    @OneToMany(mappedBy = "billingPeriod")
    private List<Expense> expenses;
    @OneToOne(mappedBy = "billingPeriod")
    private MonthSummary monthSummary;
    @ManyToOne
    private Domain domain;

    public BillingPeriod() {
    }

    public Integer getId() {
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

    public BillingPeriod setId(int id) {
        this.id = id;
        return this;
    }

    public MonthSummary getMonthSummary() {
        return monthSummary;
    }

    public BillingPeriod setMonthSummary(MonthSummary monthSummary) {
        this.monthSummary = monthSummary;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public BillingPeriod setDomain(Domain domain) {
        this.domain = domain;
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
