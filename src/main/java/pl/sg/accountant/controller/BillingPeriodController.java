package pl.sg.accountant.controller;

import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.transport.DomainTO;

import java.time.YearMonth;

public interface BillingPeriodController {

    BillingPeriodInfo currentPeriod(ApplicationUser user, int domainId);

    BillingPeriodInfo periodForMonth(ApplicationUser user, int domainId, YearMonth month);

    BillingPeriodInfo create(ApplicationUser user, DomainTO domain);

    BillingPeriodInfo create(ApplicationUser user, DomainTO domain, YearMonth month);

    BillingPeriodInfo finish(ApplicationUser user, int domainId, YearMonth month);

    String createIncome(ApplicationUser user, int accountId, IncomeTO incomeTO);

    String createExpense(ApplicationUser user, int accountId, ExpenseTO expenseTO);
}
