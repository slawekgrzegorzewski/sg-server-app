package pl.sg.accountant.controller;

import org.springframework.web.bind.annotation.PathVariable;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;

public interface BillingPeriodController {

    BillingPeriodInfo currentPeriod(Domain domain);

    BillingPeriodInfo periodForMonth(Domain domain, YearMonth month);

    BillingPeriodInfo create(Domain domain);

    BillingPeriodInfo create(Domain domain, YearMonth month);

    BillingPeriodInfo finish(Domain domain, YearMonth month);

    String createIncome(Account account, Income income);

    String createIncome(Account account, Income income, List<Integer> nodrigenTransactionsIds);

    String createExpense(Account account, Expense expense);

    String createExpense(Account account, Expense expense, List<Integer> nodrigenTransactionsIds);
}
