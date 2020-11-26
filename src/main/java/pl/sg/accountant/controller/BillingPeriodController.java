package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.service.AccountsException;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.List;

public interface BillingPeriodController {

    ResponseEntity<BillingPeriodInfo> currentPeriod(ApplicationUser user);

    ResponseEntity<BillingPeriodInfo> periodForMonth(YearMonth month, ApplicationUser user);

    ResponseEntity<BillingPeriodInfo> create(ApplicationUser user);

    ResponseEntity<BillingPeriodInfo> create(YearMonth month, ApplicationUser user);

    ResponseEntity<List<CategoryTO>> getAllCategories(ApplicationUser user);

    ResponseEntity<CategoryTO> addCategory(CategoryTO categoryTO, ApplicationUser user) throws AccountsException;

    ResponseEntity<String> createIncome(int periodId, int accountId,
                                        IncomeTO incomeTO,
                                        ApplicationUser user) throws AccountsException;

    ResponseEntity<String> createExpense(int periodId, int accountId,
                                         ExpenseTO expenseTO,
                                         ApplicationUser user) throws AccountsException;
}
