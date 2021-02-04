package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BillingPeriodsJPAService implements BillingPeriodsService {

    private final AccountsService accountsService;
    private final BillingPeriodRepository billingPeriodRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final TransactionsService transactionsService;
    private final MonthlySummaryRepository monthlySummaryRepository;
    private final PiggyBanksService piggyBanksService;

    public BillingPeriodsJPAService(AccountsService accountsService, BillingPeriodRepository billingPeriodRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository,
                                    TransactionsService transactionsService,
                                    MonthlySummaryRepository monthlySummaryRepository,
                                    PiggyBanksService piggyBanksService) {
        this.accountsService = accountsService;
        this.billingPeriodRepository = billingPeriodRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.transactionsService = transactionsService;
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.piggyBanksService = piggyBanksService;
    }

    @Override
    public BillingPeriod getById(Integer id) {
        return billingPeriodRepository.getOne(id);
    }

    @Override
    public Optional<BillingPeriod> findByPeriodAndUser(YearMonth month, ApplicationUser user) {
        return this.billingPeriodRepository.findByPeriodEqualsAndApplicationUserEquals(month, user);
    }

    @Override
    public List<BillingPeriod> unfinishedBillingPeriods(ApplicationUser user) {
        return this.billingPeriodRepository.allUnfinishedBillingPeriods(user);
    }

    @Override
    public Optional<BillingPeriod> unfinishedCurrentBillingPeriod(ApplicationUser user) {
        return this.billingPeriodRepository.unfinishedCurrentBillingPeriod(user);
    }

    @Override
    public Optional<Integer> create(YearMonth month, ApplicationUser user) {
        if (this.billingPeriodRepository.findByPeriodEqualsAndApplicationUserEquals(month, user).isPresent()) {
            return Optional.empty();
        }
        BillingPeriod period = new BillingPeriod()
                .setPeriod(month)
                .setName(month.toString())
                .setApplicationUser(user);
        billingPeriodRepository.save(period);
        return Optional.of(period.getId());
    }

    @Override
    public void addIncome(BillingPeriod billingPeriod, Account account, Income income, ApplicationUser user) throws AccountsException {
        validateCurrency(account, income.getCurrency());
        transactionsService.credit(account.getId(), income.getAmount(), income.getDescription(), user);
        income.setBillingPeriod(billingPeriod);
        if (income.getIncomeDate() == null) {
            income.setIncomeDate(LocalDate.now());
        }
        incomeRepository.save(income);
    }

    @Override
    public void addExpense(BillingPeriod billingPeriod, Account account, Expense expense, ApplicationUser user) throws AccountsException {
        validateCurrency(account, expense.getCurrency());
        validateAmount(account, expense.getAmount());
        transactionsService.debit(account.getId(), expense.getAmount(), expense.getDescription(), user);
        expense.setBillingPeriod(billingPeriod);
        if (expense.getDescription() == null) {
            expense.setExpenseDate(LocalDate.now());
        }
        expenseRepository.save(expense);
    }

    private void validateAmount(Account account, BigDecimal amount) throws AccountsException {
        if (account.getCurrentBalance().compareTo(amount) < 0) {
            throw new AccountsException("There is not enough money for that expense.");
        }
    }

    private void validateCurrency(Account account, Currency currency) throws AccountsException {
        if (!account.getCurrency().equals(currency)) {
            throw new AccountsException("Account and income currencies differ");
        }
    }

    @Override
    public void finish(BillingPeriod billingPeriod) throws AccountsException {
        Optional<MonthSummary> monthSummary = this.monthlySummaryRepository.findByBillingPeriod(billingPeriod);
        if (monthSummary.isPresent()) {
            throw new AccountsException("Already finished billing period");
        }
        ApplicationUser user = billingPeriod.getApplicationUser().getLoggedInUser();
        MonthSummary ms = new MonthSummary(billingPeriod,
                this.accountsService.getForUser(user),
                this.piggyBanksService.findByUser(billingPeriod.getApplicationUser()));
        this.monthlySummaryRepository.save(ms);

        List<PiggyBank> piggyBanks =
                this.piggyBanksService.findByUser(billingPeriod.getApplicationUser()).stream()
                .filter(pg -> pg.getMonthlyTopUp() != null)
                .filter(pg -> pg.getMonthlyTopUp().compareTo(BigDecimal.ZERO) > 0)
                .peek(PiggyBank::addMonthlyTopUp)
                .collect(Collectors.toList());
        this.piggyBanksService.updateAll(piggyBanks);
    }
}
