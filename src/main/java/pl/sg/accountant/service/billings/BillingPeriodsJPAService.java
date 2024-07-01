package pl.sg.accountant.service.billings;

import jakarta.persistence.EntityNotFoundException;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.accountant.repository.billings.BillingPeriodRepository;
import pl.sg.accountant.repository.billings.ExpenseRepository;
import pl.sg.accountant.repository.billings.IncomeRepository;
import pl.sg.accountant.repository.billings.summary.MonthlySummaryRepository;
import pl.sg.accountant.service.accounts.AccountsService;
import pl.sg.accountant.service.ledger.TransactionsService;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        addIncome2(account, income, List.of());
    }

    @Override
    public void addIncome(Account account, Income income, List<Integer> nodrigenTransactionsIds) {
        List<NodrigenTransaction> nodrigenTransactions = nodrigenTransactionRepository.findAllById(nodrigenTransactionsIds);
        for (NodrigenTransaction nodrigenTransaction : nodrigenTransactions) {
            validateNodrigenTransactionBelongsToAnAccount(account, nodrigenTransaction);
        }
        markNodrigenTransactionsCompleted(addIncome2(account, income, nodrigenTransactions));
    }

    private Map<FinancialTransaction, NodrigenTransaction> addIncome2(Account account, Income income, List<NodrigenTransaction> nodrigenTransactions) {
        BillingPeriod billingPeriod = unfinishedCurrentOrPreviousMonthBillingPeriod(account.getDomain());
        if (income.getIncomeDate() == null) {
            income.setIncomeDate(billingPeriod.getPeriod().equals(YearMonth.now()) ? LocalDate.now() : billingPeriod.getPeriod().atEndOfMonth());
        }

        validateCurrency(account, income.getCurrency());
        validateAmount(account, income.getAmount());
        validateDates(billingPeriod.getPeriod(), income.getIncomeDate());

        income.setBillingPeriod(billingPeriod);
        incomeRepository.save(income);
        return createFinancialTransactions(account, income, nodrigenTransactions);
    }

    private Map<FinancialTransaction, NodrigenTransaction> createFinancialTransactions(
            Account account,
            Income income,
            List<NodrigenTransaction> nodrigenTransactions) {
        if (nodrigenTransactions.isEmpty()) {
            Map<FinancialTransaction, NodrigenTransaction> completionMap = new HashMap<>();
            completionMap.put(
                    transactionsService.credit(
                            account,
                            income.getAmount(),
                            income.getIncomeDate().atStartOfDay(),
                            income.getDescription()),
                    null);
            return completionMap;
        }
        return nodrigenTransactions.stream()
                .filter(Predicate.not(NodrigenTransaction::isHandled))
                .collect(Collectors.toMap(
                        nodrigenTransaction -> transactionsService.credit(
                                account,
                                nodrigenTransaction.getTransactionAmount().getAmount().abs(),
                                income.getIncomeDate().atStartOfDay(),
                                income.getDescription()),
                        Function.identity()
                ));
    }

    @Override
    public void addExpense(Account account, Expense expense) {
        addExpense2(account, expense, List.of());
    }

    @Override
    public void addExpense(Account account, Expense expense, List<Integer> nodrigenTransactionsIds) {
        List<NodrigenTransaction> nodrigenTransactions = nodrigenTransactionRepository.findAllById(nodrigenTransactionsIds);
        for (NodrigenTransaction nodrigenTransaction : nodrigenTransactions) {
            validateNodrigenTransactionBelongsToAnAccount(account, nodrigenTransaction);
        }
        markNodrigenTransactionsCompleted(addExpense2(account, expense, nodrigenTransactions));
    }

    private Map<FinancialTransaction, NodrigenTransaction> addExpense2(Account account, Expense expense, List<NodrigenTransaction> nodrigenTransactions) {
        BillingPeriod billingPeriod = unfinishedCurrentOrPreviousMonthBillingPeriod(account.getDomain());
        if (expense.getExpenseDate() == null) {
            expense.setExpenseDate(billingPeriod.getPeriod().equals(YearMonth.now()) ? LocalDate.now() : billingPeriod.getPeriod().atEndOfMonth());
        }

        validateCurrency(account, expense.getCurrency());
        validateAmount(account, expense.getAmount().negate());
        validateDates(billingPeriod.getPeriod(), expense.getExpenseDate());

        expense.setBillingPeriod(billingPeriod);
        expenseRepository.save(expense);
        return getFinancialTransaction(account, expense, nodrigenTransactions);
    }

    private Map<FinancialTransaction, NodrigenTransaction> getFinancialTransaction(Account account, Expense expense, List<NodrigenTransaction> nodrigenTransactions) {

        if (nodrigenTransactions.isEmpty()) {
            Map<FinancialTransaction, NodrigenTransaction> completionMap = new HashMap<>();
            completionMap.put(
                    transactionsService.debit(account, expense.getAmount(), expense.getExpenseDate().atStartOfDay(), expense.getDescription()),
                    null);
            return completionMap;
        }
        return nodrigenTransactions.stream()
                .filter(Predicate.not(NodrigenTransaction::isHandled))
                .collect(Collectors.toMap(
                        nodrigenTransaction -> transactionsService.debit(
                                account,
                                nodrigenTransaction.getTransactionAmount().getAmount().abs(),
                                expense.getExpenseDate().atStartOfDay(),
                                expense.getDescription()),
                        Function.identity()
                ));
    }

    private void markNodrigenTransactionsCompleted(Map<FinancialTransaction, NodrigenTransaction> completionMap) {
        for (Map.Entry<FinancialTransaction, NodrigenTransaction> transactions : completionMap.entrySet()) {
            FinancialTransaction financialTransaction = transactions.getKey();
            NodrigenTransaction nodrigenTransaction = transactions.getValue();
            if (nodrigenTransaction.getTransactionAmount().getAmount().compareTo(BigDecimal.ZERO) >= 0)
                nodrigenTransaction.setCreditTransaction(financialTransaction);
            else nodrigenTransaction.setDebitTransaction(financialTransaction);
        }
        nodrigenTransactionRepository.saveAll(completionMap.values());
    }

    private BillingPeriod unfinishedCurrentOrPreviousMonthBillingPeriod(Domain domain) {
        return this.billingPeriodRepository.lastUnfinishedBillingPeriod(domain)
                .orElseThrow(() -> new EntityNotFoundException("No current billing period available to create an income"));
    }

    private void validateAmount(Account account, BigDecimal amount) {
        if (!(account.getAvailableBalance().add(Money.of(amount, account.getCurrentBalance().getCurrency().getCurrencyCode())).isPositiveOrZero())) {
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
