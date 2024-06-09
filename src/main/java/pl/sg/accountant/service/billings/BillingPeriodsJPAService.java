package pl.sg.accountant.service.billings;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.accountant.repository.billings.BillingPeriodRepository;
import pl.sg.accountant.repository.billings.ExpenseRepository;
import pl.sg.accountant.repository.billings.IncomeRepository;
import pl.sg.accountant.repository.billings.summary.MonthlySummaryRepository;
import pl.sg.accountant.service.ledger.TransactionsService;
import pl.sg.accountant.service.accounts.AccountsService;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;

import jakarta.persistence.EntityNotFoundException;

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
    private final TransactionsService transactionsService;
    private final MonthlySummaryRepository monthlySummaryRepository;
    private final NodrigenTransactionRepository nodrigenTransactionRepository;
    private final PiggyBanksService piggyBanksService;

    public BillingPeriodsJPAService(BillingPeriodRepository billingPeriodRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository,
                                    AccountsService accountsService,
                                    TransactionsService transactionsService,
                                    MonthlySummaryRepository monthlySummaryRepository,
                                    NodrigenTransactionRepository nodrigenTransactionRepository, PiggyBanksService piggyBanksService) {
        this.accountsService = accountsService;
        this.billingPeriodRepository = billingPeriodRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.transactionsService = transactionsService;
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.nodrigenTransactionRepository = nodrigenTransactionRepository;
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
                this.accountsService.getForDomain(domain.getId()),
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
        addIncome2(account, income);
    }

    @Override
    public void addIncome(Account account, Income income, List<Integer> nodrigenTransactionsIds) {
        List<NodrigenTransaction> nodrigenTransactions = nodrigenTransactionRepository.findAllById(nodrigenTransactionsIds);
        for (NodrigenTransaction nodrigenTransaction : nodrigenTransactions) {
            validateNodrigenTransactionBelongsToAnAccount(account, nodrigenTransaction);
        }
        FinancialTransaction transaction = addIncome2(account, income);
        markNodrigenTransactionsCompleted(nodrigenTransactions, transaction);
    }

    private FinancialTransaction addIncome2(Account account, Income income) {
        BillingPeriod billingPeriod = unfinishedCurrentOrPreviousMonthBillingPeriod(account.getDomain());
        if (income.getIncomeDate() == null) {
            income.setIncomeDate(billingPeriod.getPeriod().equals(YearMonth.now()) ? LocalDate.now() : billingPeriod.getPeriod().atEndOfMonth());
        }

        validateCurrency(account, income.getCurrency());
        validateAmount(account, income.getAmount());
        validateDates(billingPeriod.getPeriod(), income.getIncomeDate());

        FinancialTransaction ft = transactionsService.credit(account, income.getAmount(), income.getIncomeDate().atStartOfDay(), income.getDescription());
        income.setBillingPeriod(billingPeriod);
        incomeRepository.save(income);
        return ft;
    }

    @Override
    public void addExpense(Account account, Expense expense) {
        addExpense2(account, expense);
    }

    @Override
    public void addExpense(Account account, Expense expense, List<Integer> nodrigenTransactionsIds) {
        List<NodrigenTransaction> nodrigenTransactions = nodrigenTransactionRepository.findAllById(nodrigenTransactionsIds);
        for (NodrigenTransaction nodrigenTransaction : nodrigenTransactions) {
            validateNodrigenTransactionBelongsToAnAccount(account, nodrigenTransaction);
        }
        FinancialTransaction transaction = addExpense2(account, expense);
        markNodrigenTransactionsCompleted(nodrigenTransactions, transaction);
    }

    private FinancialTransaction addExpense2(Account account, Expense expense) {
        BillingPeriod billingPeriod = unfinishedCurrentOrPreviousMonthBillingPeriod(account.getDomain());
        if (expense.getExpenseDate() == null) {
            expense.setExpenseDate(billingPeriod.getPeriod().equals(YearMonth.now()) ? LocalDate.now() : billingPeriod.getPeriod().atEndOfMonth());
        }

        validateCurrency(account, expense.getCurrency());
        validateAmount(account, expense.getAmount().negate());
        validateDates(billingPeriod.getPeriod(), expense.getExpenseDate());

        FinancialTransaction ft = transactionsService.debit(account, expense.getAmount(), expense.getExpenseDate().atStartOfDay(), expense.getDescription());
        expense.setBillingPeriod(billingPeriod);
        expenseRepository.save(expense);
        return ft;
    }

    private void markNodrigenTransactionsCompleted(List<NodrigenTransaction> nodrigenTransactions, FinancialTransaction transaction) {
        for (NodrigenTransaction nodrigenTransaction : nodrigenTransactions) {
            if (nodrigenTransaction.getTransactionAmount().getAmount().compareTo(BigDecimal.ZERO) >= 0)
                nodrigenTransaction.setCreditTransaction(transaction);
            else nodrigenTransaction.setDebitTransaction(transaction);
        }
        nodrigenTransactionRepository.saveAll(nodrigenTransactions);
    }

    private BillingPeriod unfinishedCurrentOrPreviousMonthBillingPeriod(Domain domain) {
        return this.billingPeriodRepository.lastUnfinishedBillingPeriod(domain)
                .orElseThrow(() -> new EntityNotFoundException("No current billing period available to create an income"));
    }

    private void validateAmount(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountsException("There is not enough money for that expense.");
        }
    }

    private void validateCurrency(Account account, Currency currency) {
        if (!account.getCurrency().equals(currency)) {
            throw new AccountsException("Account and income currencies differ");
        }
    }

    private void validateNodrigenTransactionBelongsToAnAccount(Account account, NodrigenTransaction nodrigenTransaction) {
        if (!account.getId().equals(nodrigenTransaction.getBankAccount().getAccount().getId())) {
            throw new AccountsException("Nodrigen transaction not for the same account");
        }
    }

    private void validateDates(YearMonth period, LocalDate date) {
        YearMonth dateYearMonth = YearMonth.from(date);
        if (!period.equals(dateYearMonth)) {
            throw new AccountsException("Expense/Income date must be in bounds of the most recent unfinished billing period");
        }
    }
}
