package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.OperationType;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.accountant.repository.FinancialTransactionRepository;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class TransactionsJPAService implements TransactionsService {
    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final DomainService domainService;
    private final NodrigenTransactionRepository nodrigenTransactionRepository;

    public TransactionsJPAService(
            AccountRepository accountRepository,
            FinancialTransactionRepository financialTransactionRepository,
            DomainService domainService, NodrigenTransactionRepository nodrigenTransactionRepository) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.domainService = domainService;
        this.nodrigenTransactionRepository = nodrigenTransactionRepository;
    }

    @Override
    public List<FinancialTransaction> transactionsForDomain(Domain domain) {
        return financialTransactionRepository.findAllByDomain(domain);
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversion(Account source, Account destination,
                                                               BigDecimal amount, String description) {
        validateSameDomain(source.getDomain(), destination.getDomain());
        return transfer(source, destination, description, amount, amount, BigDecimal.ONE);
    }

    @Override
    public FinancialTransaction transferCashWithoutConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, String description, int firstBankTransactionId) {
        NodrigenTransaction firstTransaction = nodrigenTransactionRepository.getOne(firstBankTransactionId);
        validateSameDomain(source.getDomain(), destination.getDomain());
        validateSameDomain(source.getDomain(), firstTransaction.getBankAccount().getDomain());
        FinancialTransaction transfer = transfer(source, destination, description, amount, amount, BigDecimal.ONE);
        firstTransaction.setDebitTransaction(transfer);
        nodrigenTransactionRepository.save(firstTransaction);
        return transfer;
    }

    @Override
    public FinancialTransaction transferMoneyWithoutConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, String description, int firstBankTransactionId, int secondBankTransactionId) {
        NodrigenTransaction firstTransaction = nodrigenTransactionRepository.getOne(firstBankTransactionId);
        NodrigenTransaction secondTransaction = nodrigenTransactionRepository.getOne(secondBankTransactionId);
        validateSameDomain(source.getDomain(), destination.getDomain());
        validateSameDomain(source.getDomain(), firstTransaction.getBankAccount().getDomain());
        validateSameDomain(source.getDomain(), secondTransaction.getBankAccount().getDomain());
        FinancialTransaction transfer = transfer(source, destination, description, amount, amount, BigDecimal.ONE);
        markBankTransactions(firstTransaction, secondTransaction, transfer);
        return transfer;
    }

    private void markBankTransactions(NodrigenTransaction firstTransaction, NodrigenTransaction secondTransaction, FinancialTransaction transfer) {
        if (firstTransaction.getTransactionAmount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            firstTransaction.setCreditTransaction(transfer);
            secondTransaction.setDebitTransaction(transfer);
        } else {
            secondTransaction.setCreditTransaction(transfer);
            firstTransaction.setDebitTransaction(transfer);
        }
        nodrigenTransactionRepository.save(firstTransaction);
        nodrigenTransactionRepository.save(secondTransaction);
    }

    @Override
    public FinancialTransaction transferMoneyWithConversion(Account source, Account destination, BigDecimal amount,
                                                            BigDecimal targetAmount, BigDecimal rate, String description) {
        validateSameDomain(source.getDomain(), destination.getDomain());
        return transfer(source, destination, description, amount, targetAmount, rate);
    }

    @Override
    public FinancialTransaction transferCashWithConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, int firstBankTransactionId) {
        NodrigenTransaction firstTransaction = nodrigenTransactionRepository.getOne(firstBankTransactionId);
        validateSameDomain(source.getDomain(), destination.getDomain());
        validateSameDomain(source.getDomain(), firstTransaction.getBankAccount().getDomain());
        FinancialTransaction transfer = transfer(source, destination, description, amount, targetAmount, rate);
        firstTransaction.setDebitTransaction(transfer);
        nodrigenTransactionRepository.save(firstTransaction);
        return transfer;
    }

    @Override
    public FinancialTransaction transferMoneyWithConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, int firstBankTransactionId, int secondBankTransactionId) {
        NodrigenTransaction firstTransaction = nodrigenTransactionRepository.getOne(firstBankTransactionId);
        NodrigenTransaction secondTransaction = nodrigenTransactionRepository.getOne(secondBankTransactionId);
        validateSameDomain(source.getDomain(), destination.getDomain());
        validateSameDomain(source.getDomain(), firstTransaction.getBankAccount().getDomain());
        validateSameDomain(source.getDomain(), secondTransaction.getBankAccount().getDomain());
        FinancialTransaction transfer = transfer(source, destination, description, amount, targetAmount, rate);
        markBankTransactions(firstTransaction, secondTransaction, transfer);
        return transfer;
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

    private void validateSameDomain(Domain first, Domain second) {
        if (!Objects.equals(first.getId(), second.getId())) {
            throw new AccountsException("Payment has to be for the same currency.");
        }
    }
}
