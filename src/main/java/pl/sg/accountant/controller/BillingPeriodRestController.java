package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.service.*;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/billing-periods")
public class BillingPeriodRestController implements BillingPeriodController {

    private final BillingPeriodsService billingPeriodsService;
    private final CategoryService categoryService;
    private final TransactionsService transactionsService;
    private final AccountsService accountsService;
    private final ModelMapper mapper;

    public BillingPeriodRestController(BillingPeriodsService billingPeriodsService,
                                       CategoryService categoryService,
                                       TransactionsService transactionsService,
                                       AccountsService accountsService,
                                       ModelMapper mapper) {
        this.billingPeriodsService = billingPeriodsService;
        this.categoryService = categoryService;
        this.transactionsService = transactionsService;
        this.accountsService = accountsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodInfo> currentPeriod(@RequestUser ApplicationUser user) {
        return getBilling(YearMonth.now(), user, getUnfinishedPeriods(user));
    }

    @Override
    @GetMapping("/{period}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodInfo> periodForMonth(@PathVariable("period") YearMonth month, @RequestUser ApplicationUser user) {
        return getBilling(month, user, getUnfinishedPeriods(user));
    }

    @NotNull
    private ResponseEntity<BillingPeriodInfo> getBilling(YearMonth month,
                                                         ApplicationUser user,
                                                         @NotNull List<BillingPeriodTO> unfinishedPeriods) {
        BillingPeriodTO periodTO = this.billingPeriodsService.findByPeriodAndUser(month, user)
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .orElse(null);
        return ResponseEntity.ok(new BillingPeriodInfo(periodTO, unfinishedPeriods));
    }

    @NotNull
    private List<BillingPeriodTO> getUnfinishedPeriods(ApplicationUser user) {
        return this.billingPeriodsService.unfinishedBillingPeriods(user).stream()
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodInfo> create(@RequestUser ApplicationUser user) {
        return createBilling(YearMonth.now(), user, getUnfinishedPeriods(user));
    }

    @Override
    @PutMapping("/{period}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodInfo> create(@PathVariable("period") YearMonth month, @RequestUser ApplicationUser user) {
        return createBilling(month, user, getUnfinishedPeriods(user));
    }

    @Override
    @GetMapping("/{period}/finish")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodInfo> finish(@PathVariable("period") YearMonth month, @RequestUser ApplicationUser user) throws AccountsException {
        Optional<BillingPeriod> period = this.billingPeriodsService.findByPeriodAndUser(month, user);
        if (period.isPresent()) {
            this.billingPeriodsService.finish(period.get());
        }
        return getBilling(month, user, getUnfinishedPeriods(user));
    }

    @NotNull
    private ResponseEntity<BillingPeriodInfo> createBilling(YearMonth month, ApplicationUser user, @NotNull List<BillingPeriodTO> unfinishedPeriods) {
        return this.billingPeriodsService.create(month, user)
                .map(billingPeriodsService::getById)
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .map(period -> new BillingPeriodInfo(period, unfinishedPeriods))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @Override
    @GetMapping("/categories")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<List<CategoryTO>> getAllCategories(@RequestUser ApplicationUser user) {
        return ResponseEntity.ok(
                categoryService.getAllForUser(user).stream()
                        .map(category -> mapper.map(category, CategoryTO.class))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @PutMapping("/categories")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<CategoryTO> addCategory(@RequestBody CategoryTO categoryTO, @RequestUser ApplicationUser user) throws AccountsException {
        Category created;
        if (categoryTO.getId() == null) {
            created = categoryService.create(mapper.map(categoryTO, Category.class), user);
        } else {
            Category category = categoryService.findByIdAndApplicationUser(categoryTO.getId(), user)
                    .orElseThrow(() -> new AccountsException("Category to update does not exist."));
            mapper.map(categoryTO, category);
            created = categoryService.update(categoryTO.getId(), category, user);
        }
        return ResponseEntity.ok(mapper.map(created, CategoryTO.class));
    }

    @Override
    @PutMapping("/{periodId}/income/{accountId}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<String> createIncome(@PathVariable int periodId, @PathVariable int accountId,
                                               @RequestBody IncomeTO incomeTO,
                                               @RequestUser ApplicationUser user) throws AccountsException {
        Account account = accountsService.getById(accountId);
        BillingPeriod billingPeriod = billingPeriodsService.getById(periodId);
        Income income = mapper.map(incomeTO, Income.class);
        billingPeriodsService.addIncome(billingPeriod, account, income, user);
        return ResponseEntity.ok("OK");
    }

    @Override
    @PutMapping("/{periodId}/expense/{accountId}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<String> createExpense(@PathVariable int periodId, @PathVariable int accountId,
                                                @RequestBody ExpenseTO expenseTO,
                                                @RequestUser ApplicationUser user) throws AccountsException {
        Account account = accountsService.getById(accountId);
        BillingPeriod billingPeriod = billingPeriodsService.getById(periodId);
        Expense expense = mapper.map(expenseTO, Expense.class);
        billingPeriodsService.addExpense(billingPeriod, account, expense, user);
        return ResponseEntity.ok("OK");
    }
}
