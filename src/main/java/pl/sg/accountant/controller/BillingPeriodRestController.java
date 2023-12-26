package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.repository.BillingPeriodInfo;
import pl.sg.accountant.service.BillingPeriodsService;
import pl.sg.accountant.transport.billings.BillingPeriod;
import pl.sg.accountant.transport.billings.ExpenseTO;
import pl.sg.accountant.transport.billings.IncomeTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.*;

import jakarta.validation.constraints.NotNull;
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
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public BillingPeriodInfo currentPeriod(@RequestDomain Domain domain) {
        return getBilling(domain, YearMonth.now());
    }

    @Override
    @GetMapping("/{period}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public BillingPeriodInfo periodForMonth(@RequestDomain Domain domain,
                                            @PathVariable("period") YearMonth period) {
        return getBilling(domain, period);
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public BillingPeriodInfo create(@RequestDomain Domain domain) {
        final pl.sg.accountant.model.billings.BillingPeriod newEntity = this.billingPeriodsService.create(domain, YearMonth.now());
        return billingPeriodResponseCreator(domain).apply(newEntity);
    }

    @Override
    @PutMapping("/{period}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public BillingPeriodInfo create(@RequestDomain Domain domain,
                                    @PathVariable("period") YearMonth period) {
        final pl.sg.accountant.model.billings.BillingPeriod newEntity = this.billingPeriodsService.create(domain, period);
        return billingPeriodResponseCreator(domain).apply(newEntity);
    }

    @Override
    @PatchMapping("/{period}/finish")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public BillingPeriodInfo finish(@RequestDomain Domain domain,
                                    @PathVariable("period") YearMonth month) {
        this.billingPeriodsService.finishBillingPeriod(domain, month);
        return getBilling(domain, month);
    }

    @Override
    @PutMapping("/income/{account}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String createIncome(@PathVariableWithDomain("account") Account account,
                               @MapRequestBody(transportClass = IncomeTO.class) Income income) {
        billingPeriodsService.addIncome(account, income);
        return "OK";
    }

    @Override
    @PutMapping("/income/{account}/{nodrigenTransactionsIds}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String createIncome(@PathVariableWithDomain("account") Account account,
                               @MapRequestBody(transportClass = IncomeTO.class) Income income,
                               @PathVariable("nodrigenTransactionsIds") List<Integer> nodrigenTransactionsIds) {
        billingPeriodsService.addIncome(account, income, nodrigenTransactionsIds);
        return "OK";
    }

    @Override
    @PutMapping("/expense/{account}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String createExpense(@PathVariableWithDomain("account") Account account,
                                @MapRequestBody(transportClass = ExpenseTO.class) Expense expense) {
        billingPeriodsService.addExpense(account, expense);
        return "OK";
    }

    @Override
    @PutMapping("/expense/{account}/{nodrigenTransactionsIds}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String createExpense(@PathVariableWithDomain("account") Account account,
                                @MapRequestBody(transportClass = ExpenseTO.class) Expense expense,
                                @PathVariable("nodrigenTransactionsIds") List<Integer> nodrigenTransactionsIds) {
        billingPeriodsService.addExpense(account, expense, nodrigenTransactionsIds);
        return "OK";
    }

    @NotNull
    private BillingPeriodInfo getBilling(Domain domain, YearMonth month) {
        BillingPeriod periodTO = this.billingPeriodsService.findByPeriodAndDomain(domain, month)
                .map(period -> mapper.map(period, BillingPeriod.class))
                .orElse(null);
        return new BillingPeriodInfo(periodTO, getUnfinishedPeriods(domain));
    }

    private Function<pl.sg.accountant.model.billings.BillingPeriod, BillingPeriodInfo> billingPeriodResponseCreator(Domain domain) {
        return billingPeriod -> {
            final BillingPeriod byIdTO = mapper.map(billingPeriod, BillingPeriod.class);
            return new BillingPeriodInfo(byIdTO, getUnfinishedPeriods(domain));
        };
    }

    private List<BillingPeriod> getUnfinishedPeriods(Domain domain) {
        return this.billingPeriodsService.unfinishedBillingPeriods(domain).stream()
                .map(period -> mapper.map(period, BillingPeriod.class))
                .collect(Collectors.toList());
    }
}
