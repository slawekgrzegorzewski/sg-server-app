package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.service.AccountsService;
import pl.sg.accountant.service.BillingPeriodsService;
import pl.sg.accountant.service.MonthSummaryService;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_ACCOUNT;
import static pl.sg.Application.UPDATE_ACCOUNT;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountsRestController implements AccountsController {
    private final AccountsService accountsService;
    private final ModelMapper mapper;

    private final MonthSummaryService monthSummaryService;
    private final BillingPeriodsService billingPeriodsService;

    public AccountsRestController(AccountsService accountsService, ModelMapper mapper, MonthSummaryService monthSummaryService, BillingPeriodsService billingPeriodsService) {
        this.accountsService = accountsService;
        this.mapper = mapper;
        this.monthSummaryService = monthSummaryService;
        this.billingPeriodsService = billingPeriodsService;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN"})
    public List<AccountTO> allAccounts() {
        return map(accountsService.getAll());
    }

    @Override
    @GetMapping("/mine")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<AccountTO> domainAccount(@RequestDomain Domain domain) {

        Map<YearMonth, List<MonthSummaryPiggyBank>> piggyBanksHistory = monthSummaryService.getPiggyBanksHistory(domain, 24);
        Map<YearMonth, Map<Currency, BigDecimal>> savingsHistory = monthSummaryService.getSavingsHistory(domain, 24);


        Map<Currency, Double> incomes = new HashMap<>();

        Map<Currency, Double> expenses = new HashMap<>();

        piggyBanksHistory.keySet().stream()
                .sorted()
                .filter(ym -> (ym.getYear() == 2021 && ym.getMonthValue() <= 5)
                        || (ym.getYear() == 2020 && ym.getMonthValue() >= 11))
                .forEach(ym -> {
                    BillingPeriod billingPeriod = billingPeriodsService.getByPeriodAndDomain(domain, ym);

                    billingPeriod.getIncomes()
                            .forEach(i -> {
                                incomes.compute(i.getCurrency(), (currency, sum) -> (sum == null ? 0 : sum) + i.getAmount().doubleValue());
                            });

                    billingPeriod.getExpenses()
                            .forEach(e -> {
                                expenses.compute(e.getCurrency(), (currency, sum) -> (sum == null ? 0 : sum) + e.getAmount().doubleValue());
                            });

                    Optional<BigDecimal> sumOfPiggyBanksNoSavings = piggyBanksHistory.get(ym).stream()
                            .filter(pg -> Objects.equals(pg.currency.getCurrencyCode(), "PLN"))
                            .filter(pg -> !pg.savings)
                            .map(pg -> pg.balance)
                            .reduce(BigDecimal::add);

                    Optional<BigDecimal> sumOfPiggyBanksSavings = piggyBanksHistory.get(ym).stream()
                            .filter(pg -> Objects.equals(pg.currency.getCurrencyCode(), "PLN"))
                            .filter(pg -> pg.savings)
                            .map(pg -> pg.balance)
                            .reduce(BigDecimal::add);
                });

        return map(accountsService.getForDomain(domain));
    }

    private List<AccountTO> map(List<Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, AccountTO.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public AccountTO createAccount(
            @RequestBodyWithDomain(
                    transportClass = AccountTO.class,
                    mapperName = CREATE_ACCOUNT,
                    create = true,
                    domainAdmin = true)
            @Valid Account account
    ) {
        accountsService.createAccount(account);
        return mapper.map(account, AccountTO.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String updateAccount(
            @RequestBodyWithDomain(transportClass = AccountTO.class, mapperName = UPDATE_ACCOUNT) @Valid Account account
    ) {
        accountsService.update(account);
        return "OK";
    }

    @Override
    @DeleteMapping("/{account}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteAccount(@PathVariableWithDomain(requireAdmin = true) Account account) {
        accountsService.delete(account);
        return "Deleted.";
    }
}
