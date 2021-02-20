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
    public BillingPeriod getByPeriodAndDomain(Domain domain, YearMonth month) {
        return this.findByPeriodAndDomain(domain, month).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<BillingPeriod> findByPeriodAndDomain(Domain domain, YearMonth month) {
        return this.billingPeriodRepository.findByDomainAndPeriod(domain, month);
    }

    @Override
    public List<BillingPeriod> unfinishedBillingPeriods(Domain domain) {
        return this.billingPeriodRepository.allUnfinishedBillingPeriods(domain);
    }

    @Override
    public BillingPeriod create(Domain domain, YearMonth month) {
        if (findByPeriodAndDomain(domain, month).isPresent()) {
            throw new AccountsException("Billing period for this period already exists.");
        }
        return billingPeriodRepository.save(
                new BillingPeriod()
                        .setPeriod(month)
                        .setName(month.toString())
                        .setDomain(domain));
    }

    @Override
    public void finishBillingPeriod(Domain domain, YearMonth month) {
        BillingPeriod billingPeriod = getByPeriodAndDomain(domain, month);
        Optional<MonthSummary> monthSummary = this.monthlySummaryRepository.findByBillingPeriod(billingPeriod);
        if (monthSummary.isPresent()) {
            throw new AccountsException("Already finished billing period");
        }
        List<PiggyBank> piggyBanks = this.piggyBanksService.findByDomain(domain);
        MonthSummary ms = new MonthSummary(billingPeriod,
                this.accountsService.getForDomain(domain),
                piggyBanks);
        this.monthlySummaryRepository.save(ms);

        piggyBanks.stream()
                .filter(pg -> pg.getMonthlyTopUp() != null)
                .filter(pg -> pg.getMonthlyTopUp().compareTo(BigDecimal.ZERO) > 0)
                .forEach(PiggyBank::addMonthlyTopUp);

        this.piggyBanksService.updateAll(piggyBanks);
    }

    @Override
    public void addIncome(Account account, Income income) {
        BillingPeriod billingPeriod = unfinishedCurrentBillingPeriod(account.getDomain());

        validateCurrency(account, income.getCurrency());

        transactionsService.credit(account, income.getAmount(), income.getDescription());
        income.setBillingPeriod(billingPeriod);
        if (income.getIncomeDate() == null) {
            income.setIncomeDate(LocalDate.now());
        }
        incomeRepository.save(income);
    }

    @Override
    public void addExpense(Account account, Expense expense) {
        BillingPeriod billingPeriod = unfinishedCurrentBillingPeriod(account.getDomain());

        validateCurrency(account, expense.getCurrency());
        validateAmount(account, expense.getAmount());

        transactionsService.debit(account, expense.getAmount(), expense.getDescription());
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
}
