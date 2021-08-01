package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.OperationType;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;
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
    public List<FinancialTransaction> transactionsForDomain(Domain domain) {
        return financialTransactionRepository.findAllByDomain(domain);
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversion(Account source, Account destination,
                                                               BigDecimal amount, String description) {
        return transfer(source, destination, description, amount, amount, BigDecimal.ONE);
    }

    @Override
    public FinancialTransaction transferMoneyWithConversion(Account source, Account destination, BigDecimal amount,
                                                            BigDecimal targetAmount, BigDecimal rate, String description) {
        return transfer(source, destination, description, amount, targetAmount, rate);
    }

    private FinancialTransaction transfer(Account source, Account destination,
                                          String description,
                                          BigDecimal amount,
                                          BigDecimal targetAmount,
                                          BigDecimal rate) {
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(LocalDateTime.now())
                .setDescription(description)
                .setTimeOfTransaction(LocalDateTime.now());
        if (source.getCurrency() == destination.getCurrency()) {
            financialTransaction.transfer(source, destination, amount);
        } else {
            financialTransaction.transfer(source, destination, amount, targetAmount, rate);
        }
        financialTransaction = financialTransactionRepository.save(financialTransaction);
        source.debit(financialTransaction);
        destination.credit(financialTransaction);
        accountRepository.saveAll(List.of(source, destination));
        return financialTransaction;
    }

    @Override
    public FinancialTransaction credit(Account account, BigDecimal amount, String description) {
        return operation(account, amount, LocalDateTime.now(), description, OperationType.CREDIT);
    }

    @Override
    public FinancialTransaction credit(Account account, BigDecimal amount, LocalDateTime transactionDate, String description) {
        return operation(account, amount, transactionDate, description, OperationType.CREDIT);
    }

    @Override
    public FinancialTransaction debit(Account account, BigDecimal amount, String description) {
        return operation(account, amount, LocalDateTime.now(), description, OperationType.DEBIT);
    }

    @Override
    public FinancialTransaction debit(Account account, BigDecimal amount, LocalDateTime transactionDate, String description) {
        return operation(account, amount, transactionDate, description, OperationType.DEBIT);
    }

    private FinancialTransaction operation(Account account, BigDecimal amount, LocalDateTime transactionDate, String description, OperationType type) {
        FinancialTransaction financialTransaction = new FinancialTransaction()
                .setTimeOfTransaction(transactionDate)
                .transfer(account, amount, type)
                .setDescription(description);
        financialTransaction = financialTransactionRepository.save(financialTransaction);
        type.getOperation().accept(account, financialTransaction);
        accountRepository.save(account);
        return financialTransaction;
    }
}
