package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Expense;
import pl.sg.accountant.model.billings.Income;
import pl.sg.accountant.repository.BillingPeriodRepository;
import pl.sg.accountant.repository.ExpenseRepository;
import pl.sg.accountant.repository.IncomeRepository;
import pl.sg.application.model.ApplicationUser;

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
    private final TransactionsService transactionsService;

    public BillingPeriodsJPAService(BillingPeriodRepository billingPeriodRepository,
                                    IncomeRepository incomeRepository,
                                    ExpenseRepository expenseRepository,
                                    TransactionsService transactionsService) {
        this.billingPeriodRepository = billingPeriodRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.transactionsService = transactionsService;
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
}
