package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.service.BillingPeriodsService;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.transport.DomainTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/billing-periods")
public class BillingPeriodRestController implements BillingPeriodController {

    private final BillingPeriodsService billingPeriodsService;
    private final ModelMapper mapper;

    public BillingPeriodRestController(BillingPeriodsService billingPeriodsService,
                                       ModelMapper mapper) {
        this.billingPeriodsService = billingPeriodsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping("/{domainId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<BillingPeriodInfo> currentPeriod(@RequestUser ApplicationUser user,
                                                           @PathVariable("domainId") int domainId) {
        return getBilling(user, domainId, YearMonth.now());
    }

    @Override
    @GetMapping("/{domainId}/{period}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<BillingPeriodInfo> periodForMonth(@RequestUser ApplicationUser user,
                                                            @PathVariable("domainId") int domainId,
                                                            @PathVariable("period") YearMonth month) {
        return getBilling(user, domainId, month);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<BillingPeriodInfo> create(@RequestUser ApplicationUser user,
                                                    @RequestBody @Valid DomainTO domain) {
        return this.billingPeriodsService.create(user, domain.getId(), YearMonth.now())
                .map(billingPeriodResponseCreator(user, domain.getId()))
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @Override
    @PutMapping("/{period}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<BillingPeriodInfo> create(@RequestUser ApplicationUser user,
                                                    @RequestBody @Valid DomainTO domain,
                                                    @PathVariable("period") YearMonth month) {
        return this.billingPeriodsService.create(user, domain.getId(), month)
                .map(billingPeriodResponseCreator(user, domain.getId()))
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @Override
    @GetMapping("/{domainId}/{period}/finish")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<BillingPeriodInfo> finish(@RequestUser ApplicationUser user,
                                                    @PathVariable("domainId") int domainId,
                                                    @PathVariable("period") YearMonth month) {
        this.billingPeriodsService.finishBillingPeriod(user, domainId, month);
        return getBilling(user, domainId, month);
    }

    @Override
    @PutMapping("/income/{accountId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> createIncome(@RequestUser ApplicationUser user,
                                               @PathVariable int accountId,
                                               @RequestBody IncomeTO incomeTO) {
        Income income = mapper.map(incomeTO, Income.class);
        billingPeriodsService.addIncome(user, accountId, income);
        return ResponseEntity.ok("OK");
    }

    @Override
    @PutMapping("/expense/{accountId}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public ResponseEntity<String> createExpense(@RequestUser ApplicationUser user,
                                                @PathVariable int accountId,
                                                @RequestBody ExpenseTO expenseTO) {
        Expense expense = mapper.map(expenseTO, Expense.class);
        billingPeriodsService.addExpense(user, accountId, expense);
        return ResponseEntity.ok("OK");
    }


    @NotNull
    private ResponseEntity<BillingPeriodInfo> getBilling(ApplicationUser user, int domainId, YearMonth month) {
        BillingPeriodTO periodTO = this.billingPeriodsService.findByPeriodAndDomain(user, domainId, month)
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .orElse(null);
        return ResponseEntity.ok(new BillingPeriodInfo(periodTO, getUnfinishedPeriods(user, domainId)));
    }

    private Function<Integer, ResponseEntity<BillingPeriodInfo>> billingPeriodResponseCreator(ApplicationUser user, int domainId) {
        return id -> {
            final BillingPeriod byId = billingPeriodsService.getById(user, id);
            final BillingPeriodTO byIdTO = mapper.map(byId, BillingPeriodTO.class);
            return ResponseEntity.ok(new BillingPeriodInfo(byIdTO, getUnfinishedPeriods(user, domainId)));
        };
    }

    private List<BillingPeriodTO> getUnfinishedPeriods(ApplicationUser user, int domainId) {
        return this.billingPeriodsService.unfinishedBillingPeriods(user, domainId).stream()
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .collect(Collectors.toList());
    }
}
