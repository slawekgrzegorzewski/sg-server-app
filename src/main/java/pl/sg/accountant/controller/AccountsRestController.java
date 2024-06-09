package pl.sg.accountant.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.service.accounts.AccountsService;
import pl.sg.accountant.service.billings.BillingPeriodsService;
import pl.sg.accountant.service.billings.summary.MonthSummaryService;
import pl.sg.accountant.transport.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.application.security.annotations.PathVariableWithDomain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.time.YearMonth;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_ACCOUNT;
import static pl.sg.Application.UPDATE_ACCOUNT;

@RestController
@RequestMapping("/accounts")
@Validated
public class AccountsRestController implements AccountsController {
    private final AccountsService accountsService;
    private final BillingPeriodsService billingPeriodsService;
    private final DomainRepository domainRepository;
    private final ModelMapper mapper;
    private final MonthSummaryService monthSummaryService;


    public AccountsRestController(AccountsService accountsService, BillingPeriodsService billingPeriodsService, DomainRepository domainRepository, ModelMapper mapper, MonthSummaryService monthSummaryService) {
        this.accountsService = accountsService;
        this.domainRepository = domainRepository;
        this.mapper = mapper;
        this.monthSummaryService = monthSummaryService;
        this.billingPeriodsService = billingPeriodsService;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN"})
    public List<Account> allAccounts() {
        return map(accountsService.getAll());
    }

    @Override
    @GetMapping("/mine")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<Account> domainAccount(@RequestDomain Domain domain) {

        Map<YearMonth, List<MonthSummaryPiggyBank>> piggyBanksHistory = monthSummaryService.getPiggyBanksHistory(domain, 24);

        Map<Currency, Double> incomes = new HashMap<>();

        Map<Currency, Double> expenses = new HashMap<>();

        piggyBanksHistory.keySet().stream()
                .sorted()
                .filter(ym -> (ym.getYear() == 2021 && ym.getMonthValue() <= 5)
                        || (ym.getYear() == 2020 && ym.getMonthValue() >= 11))
                .forEach(ym -> {
                    BillingPeriod billingPeriod = billingPeriodsService.getByPeriodAndDomain(domain, ym);

                    billingPeriod.getIncomes()
                            .forEach(i -> incomes.compute(i.getCurrency(), (currency, sum) -> (sum == null ? 0 : sum) + i.getAmount().doubleValue()));

                    billingPeriod.getExpenses()
                            .forEach(e -> expenses.compute(e.getCurrency(), (currency, sum) -> (sum == null ? 0 : sum) + e.getAmount().doubleValue()));
                });

        return map(accountsService.getForDomain(domain.getId()));
    }

    private List<Account> map(List<pl.sg.accountant.model.accounts.Account> accounts) {
        return accounts.stream().map(a -> mapper.map(a, Account.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"}, domainAdmin = true)
    public Account createAccount(
            @RequestBodyWithDomain(
                    transportClass = Account.class,
                    mapperName = CREATE_ACCOUNT,
                    create = true,
                    domainAdmin = true)
            @Valid pl.sg.accountant.model.accounts.Account account
    ) {
        accountsService.createAccount(account);
        return mapper.map(account, Account.class);
    }

    @Override
    @PatchMapping
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String updateAccount(
            @RequestBodyWithDomain(transportClass = Account.class, mapperName = UPDATE_ACCOUNT) @Valid pl.sg.accountant.model.accounts.Account account
    ) {
        accountsService.update(account);
        return "OK";
    }

    @Override
    @DeleteMapping("/{account}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteAccount(@PathVariableWithDomain(requireAdmin = true, name = "account") pl.sg.accountant.model.accounts.Account account) {
        accountsService.delete(account);
        return "Deleted.";
    }
}
