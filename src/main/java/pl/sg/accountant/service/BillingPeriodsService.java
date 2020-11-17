package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.Optional;

public interface BillingPeriodsService {
    BillingPeriod getById(Integer id);

    Optional<BillingPeriod> findByPeriodAndUser(YearMonth month, ApplicationUser user);

    Optional<Integer> create(YearMonth month, ApplicationUser user);

    void addIncome(BillingPeriod billingPeriod, Account account, Income income, ApplicationUser user) throws AccountsException;

    void addExpense(BillingPeriod billingPeriod, Account account, Expense expense, ApplicationUser user) throws AccountsException;
}
