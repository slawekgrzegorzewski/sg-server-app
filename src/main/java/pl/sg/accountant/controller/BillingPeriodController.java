package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.transport.DomainTO;

import java.time.YearMonth;
import java.util.List;

public interface BillingPeriodController {

    ResponseEntity<BillingPeriodInfo> currentPeriod(ApplicationUser user, int domainId);

    ResponseEntity<BillingPeriodInfo> periodForMonth(ApplicationUser user, int domainId, YearMonth month);

    ResponseEntity<BillingPeriodInfo> create(ApplicationUser user, DomainTO domain);

    ResponseEntity<BillingPeriodInfo> create(ApplicationUser user, DomainTO domain, YearMonth month);

    ResponseEntity<BillingPeriodInfo> finish(ApplicationUser user, int domainId, YearMonth month);

    ResponseEntity<String> createIncome(ApplicationUser user, int accountId, IncomeTO incomeTO);

    ResponseEntity<String> createExpense(ApplicationUser user, int accountId, ExpenseTO expenseTO);
}
