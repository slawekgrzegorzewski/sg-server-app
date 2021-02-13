package pl.sg.accountant.transport.billings;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;

public class BillingPeriodTO {
    private Integer id;
    private String name;
    private YearMonth period;
    private List<IncomeTO> incomes;
    private List<ExpenseTO> expenses;
    @NotNull
    private int userId;

    public BillingPeriodTO() {
    }

    public BillingPeriodTO(int id, String name, YearMonth period, List<IncomeTO> incomes, List<ExpenseTO> expenses, @NotNull int userId) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.incomes = incomes;
        this.expenses = expenses;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public List<IncomeTO> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<IncomeTO> incomes) {
        this.incomes = incomes;
    }

    public List<ExpenseTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseTO> expenses) {
        this.expenses = expenses;
    }

    public Integer getUserId() {
        return userId;
    }

    public BillingPeriodTO setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
}
