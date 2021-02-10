package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BillingPeriodsService {
    BillingPeriod getById(ApplicationUser user, Integer id);

    BillingPeriod getByPeriodAndDomain(ApplicationUser user, int domainId, YearMonth month);

    Optional<BillingPeriod> findByPeriodAndDomain(ApplicationUser user, int domainId, YearMonth month);

    List<BillingPeriod> unfinishedBillingPeriods(ApplicationUser user, int domainId);

    Optional<Integer> create(ApplicationUser user, int domainId, YearMonth month);

    void addIncome(ApplicationUser user, int accountId, Income income);

    void addExpense(ApplicationUser user, int accountId, Expense expense);

    void finishBillingPeriod(ApplicationUser user, int domainId, YearMonth month);
}
