package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BillingPeriodsService {
    BillingPeriod getById(ApplicationUser user, Integer id);

    BillingPeriod getByPeriodAndDomain(Domain domain, YearMonth month);

    Optional<BillingPeriod> findByPeriodAndDomain(Domain domain, YearMonth month);

    List<BillingPeriod> unfinishedBillingPeriods(Domain domain);

    BillingPeriod create(Domain domain, YearMonth month);

    void finishBillingPeriod(Domain domain, YearMonth month);

    void addIncome(Account account, Income income);

    void addIncome(Account account, Income income, List<Integer> nodrigenTransactionsIds);

    void addExpense(Account account, Expense expense);

    void addExpense(Account account, Expense expense, List<Integer> nodrigenTransactionsIds);
}
