package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.accountant.repository.BillingPeriodRepository;
import pl.sg.accountant.repository.ExpenseRepository;
import pl.sg.accountant.repository.IncomeRepository;
import pl.sg.accountant.repository.MonthlySummaryRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Component
public class BillingPeriodsJPAService implements BillingPeriodsService {

    private final BillingPeriodRepository billingPeriodRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    private final AccountsService accountsService;
    private final DomainService domainService;
    private final TransactionsService transactionsService;
    private final MonthlySummaryRepository monthlySummaryRepository;
    private final PiggyBanksService piggyBanksService;

    public BillingPeriodsJPAService(BillingPeriodRepository billingPeriodRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository,
                                    AccountsService accountsService,
                                    DomainService domainService,
                                    TransactionsService transactionsService,
                                    MonthlySummaryRepository monthlySummaryRepository,
                                    PiggyBanksService piggyBanksService) {
        this.accountsService = accountsService;
        this.billingPeriodRepository = billingPeriodRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.domainService = domainService;
        this.transactionsService = transactionsService;
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.piggyBanksService = piggyBanksService;
    }

    @Override
    public BillingPeriod getById(ApplicationUser user, Integer id) {
        final BillingPeriod result = billingPeriodRepository.getOne(id);
        user.validateDomain(result.getDomain());
        return result;
    }

    @Override
    public BillingPeriod getByPeriodAndDomain(ApplicationUser user, int domainId, YearMonth month) {
        return this.findByPeriodAndDomain(user, domainId, month)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<BillingPeriod> findByPeriodAndDomain(ApplicationUser user, int domainId, YearMonth month) {
        return this.billingPeriodRepository.findByDomainIdAndPeriod(domainId, month)
                .map(byId -> {
                    user.validateDomain(byId.getDomain());
                    return byId;
                });
    }

    @Override
    public List<BillingPeriod> unfinishedBillingPeriods(ApplicationUser user, int domainId) {
        final Domain domain = domainService.getById(domainId);
        user.validateDomain(domain);
        return this.billingPeriodRepository.allUnfinishedBillingPeriods(domain);
    }

    @Override
    public Optional<Integer> create(ApplicationUser user, int domainId, YearMonth month) {
        if (findByPeriodAndDomain(user, domainId, month).isPresent()) {
            return Optional.empty();
        }
        Domain domain = domainService.getById(domainId);
        user.validateAdminDomain(domain);
        return Optional.of(billingPeriodRepository.save(
                new BillingPeriod()
                        .setPeriod(month)
                        .setName(month.toString())
                        .setDomain(domain))
                .getId());
    }

    @Override
    public void addIncome(ApplicationUser user, int accountId, Income income) {
        Account account = accountsService.getById(user, accountId);
        BillingPeriod billingPeriod = unfinishedCurrentBillingPeriod(account.getDomain());

        user.validateDomain(account.getDomain());
        user.validateDomain(billingPeriod.getDomain());
        validateCurrency(account, income.getCurrency());

        transactionsService.credit(account.getId(), income.getAmount(), income.getDescription(), user);
        income.setBillingPeriod(billingPeriod);
        if (income.getIncomeDate() == null) {
            income.setIncomeDate(LocalDate.now());
        }
        incomeRepository.save(income);
    }

    @Override
    public void addExpense(ApplicationUser user, int accountId, Expense expense) {
        Account account = accountsService.getById(user, accountId);
        BillingPeriod billingPeriod = unfinishedCurrentBillingPeriod(account.getDomain());

        user.validateDomain(account.getDomain());
        user.validateDomain(billingPeriod.getDomain());
        validateCurrency(account, expense.getCurrency());
        validateAmount(account, expense.getAmount());

        transactionsService.debit(account.getId(), expense.getAmount(), expense.getDescription(), user);
        expense.setBillingPeriod(billingPeriod);
        if (expense.getDescription() == null) {
            expense.setExpenseDate(LocalDate.now());
        }
        expenseRepository.save(expense);
    }

    private BillingPeriod unfinishedCurrentBillingPeriod(Domain domain) {
        return this.billingPeriodRepository.unfinishedCurrentBillingPeriod(domain)
                .orElseThrow(() -> new EntityNotFoundException("No current billing period available to create an income"));
    }

    private void validateAmount(Account account, BigDecimal amount) {
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountsException("There is not enough money for that expense.");
        }
    }

    private void validateCurrency(Account account, Currency currency) {
        if (!account.getCurrency().equals(currency)) {
            throw new AccountsException("Account and income currencies differ");
        }
    }

    @Override
    public void finishBillingPeriod(ApplicationUser user, int domainId, YearMonth month) {
        BillingPeriod billingPeriod = getByPeriodAndDomain(user, domainId, month);
        Optional<MonthSummary> monthSummary = this.monthlySummaryRepository.findByBillingPeriod(billingPeriod);
        if (monthSummary.isPresent()) {
            throw new AccountsException("Already finished billing period");
        }
        List<PiggyBank> piggyBanks = this.piggyBanksService.findByDomain(user, domainId);
        MonthSummary ms = new MonthSummary(billingPeriod,
                this.accountsService.getForUserAndDomain(user, domainId),
                piggyBanks);
        this.monthlySummaryRepository.save(ms);

        piggyBanks.stream()
                .filter(pg -> pg.getMonthlyTopUp() != null)
                .filter(pg -> pg.getMonthlyTopUp().compareTo(BigDecimal.ZERO) > 0)
                .forEach(PiggyBank::addMonthlyTopUp);

        this.piggyBanksService.updateAll(user, piggyBanks);
    }
}
