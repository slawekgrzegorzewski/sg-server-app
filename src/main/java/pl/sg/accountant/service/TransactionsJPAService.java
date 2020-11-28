package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.OperationType;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;
import pl.sg.application.model.ApplicationUser;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TransactionsJPAService implements TransactionsService {
    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    public TransactionsJPAService(
            AccountRepository accountRepository,
            FinancialTransactionRepository financialTransactionRepository) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
    }

    @Override
    public List<FinancialTransaction> transactionsForUser(String login) {
        return financialTransactionRepository.findAllByLogin(login);
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount,
                                                               BigDecimal amount, String description,
                                                               ApplicationUser user) throws AccountsException {
        return transfer(sourceAccount, destinationAccount, description, user, amount, amount, BigDecimal.ONE);
    }

    @Override
    public FinancialTransaction transferMoneyWithConversion(int sourceAccount, int destinationAccount, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, ApplicationUser user) throws AccountsException {
        return transfer(sourceAccount, destinationAccount, description, user, amount, targetAmount, rate);
    }

    private FinancialTransaction transfer(int sourceAccount, int destinationAccount, String description, ApplicationUser user, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate) throws AccountsException {
        Account from = accountRepository.getOne(sourceAccount);
        Account to = accountRepository.getOne(destinationAccount);
        validateUserOwnsTheAccount(from, user);
        validateUserOwnsTheAccount(to, user);
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(LocalDateTime.now())
                .setDescription(description)
                .setTimeOfTransaction(LocalDateTime.now())
                .setApplicationUser(user);
        if (amount.equals(targetAmount)) {
            financialTransaction.transfer(from, to, amount);
        } else {
            financialTransaction.transfer(from, to, amount, targetAmount, rate);
        }
        financialTransaction = financialTransactionRepository.save(financialTransaction);
        from.debit(financialTransaction);
        to.credit(financialTransaction);
        accountRepository.saveAll(List.of(from, to));
        return financialTransaction;
    }

    @Override
    public FinancialTransaction credit(int accountId, BigDecimal amount, String description, ApplicationUser user) throws AccountsException {
        return operation(accountId, amount, description, user, OperationType.CREDIT);
    }

    @Override
    public FinancialTransaction debit(int accountId, BigDecimal amount, String description, ApplicationUser user) throws AccountsException {
        return operation(accountId, amount, description, user, OperationType.DEBIT);
    }

    private FinancialTransaction operation(int accountId, BigDecimal amount, String description, ApplicationUser user, OperationType type) throws AccountsException {
        Account account = accountRepository.getOne(accountId);
        validateUserOwnsTheAccount(account, user);
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(LocalDateTime.now())
                .transfer(account, amount, type)
                .setDescription(description)
                .setApplicationUser(user);
        financialTransaction = financialTransactionRepository.save(financialTransaction);
        type.getOperation().accept(account, financialTransaction);
        accountRepository.save(account);
        return financialTransaction;
    }

    private ApplicationUser validateUserOwnsTheAccount(Account account, ApplicationUser user) throws AccountsException {
        if (!accountBelongsTo(account, user)) {
            throw new AccountsException("Trying to send money between accounts which does not belong to the user.");
        }
        return user;
    }

    private boolean accountBelongsTo(Account account, ApplicationUser user) {
        return account.getApplicationUser().getId() == user.getId();
    }
}
