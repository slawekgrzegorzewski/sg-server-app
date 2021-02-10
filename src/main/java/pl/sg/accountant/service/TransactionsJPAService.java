package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.OperationType;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TransactionsJPAService implements TransactionsService {
    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final DomainService domainService;

    public TransactionsJPAService(
            AccountRepository accountRepository,
            FinancialTransactionRepository financialTransactionRepository,
            DomainService domainService) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.domainService = domainService;
    }

    @Override
    public List<FinancialTransaction> transactionsForUserAndDomain(ApplicationUser user, int domainId) {
        final Domain domain = domainService.getById(domainId);
        user.validateDomain(domain);
        return financialTransactionRepository.findAllByDomain(domain);
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount,
                                                               BigDecimal amount, String description,
                                                               ApplicationUser user) {
        return transfer(sourceAccount, destinationAccount, description, user, amount, amount, BigDecimal.ONE);
    }

    @Override
    public FinancialTransaction transferMoneyWithConversion(int sourceAccount, int destinationAccount, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, ApplicationUser user) {
        return transfer(sourceAccount, destinationAccount, description, user, amount, targetAmount, rate);
    }

    private FinancialTransaction transfer(int sourceAccount,
                                          int destinationAccount,
                                          String description,
                                          ApplicationUser user,
                                          BigDecimal amount,
                                          BigDecimal targetAmount,
                                          BigDecimal rate) {
        Account from = accountRepository.getOne(sourceAccount);
        Account to = accountRepository.getOne(destinationAccount);
        user.validateDomain(from.getDomain());
        user.validateDomain(to.getDomain());
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(LocalDateTime.now())
                .setDescription(description)
                .setTimeOfTransaction(LocalDateTime.now());
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
    public FinancialTransaction credit(int accountId, BigDecimal amount, String description, ApplicationUser user) {
        return operation(accountId, amount, description, user, OperationType.CREDIT);
    }

    @Override
    public FinancialTransaction debit(int accountId, BigDecimal amount, String description, ApplicationUser user) {
        return operation(accountId, amount, description, user, OperationType.DEBIT);
    }

    private FinancialTransaction operation(int accountId, BigDecimal amount, String description, ApplicationUser user, OperationType type) {
        Account account = accountRepository.getOne(accountId);
        user.validateDomain(account.getDomain());
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(LocalDateTime.now())
                .transfer(account, amount, type)
                .setDescription(description);
        financialTransaction = financialTransactionRepository.save(financialTransaction);
        type.getOperation().accept(account, financialTransaction);
        accountRepository.save(account);
        return financialTransaction;
    }
}
