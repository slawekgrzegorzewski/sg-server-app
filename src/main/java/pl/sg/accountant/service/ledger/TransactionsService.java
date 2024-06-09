package pl.sg.accountant.service.ledger;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.application.model.Domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionsService {
    List<FinancialTransaction> transactionsForDomain(Domain domain);

    FinancialTransaction transferMoneyWithoutConversion(Account source, Account destination, BigDecimal amount,
                                                        String description);

    FinancialTransaction transferCashWithoutConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, String description, int firstBankTransactionId);
    FinancialTransaction transferMoneyWithoutConversionWithBankTransactions(Account source, Account destination, BigDecimal amount,
                                                        String description, int firstBankTransactionId, int secondBankTransactionId);

    FinancialTransaction transferMoneyWithConversion(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount,
                                                     BigDecimal rate, String description);

    FinancialTransaction transferCashWithConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, int firstBankTransactionId);
    FinancialTransaction transferMoneyWithConversionWithBankTransactions(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount,
                                                     BigDecimal rate, String description, int firstBankTransactionId, int secondBankTransactionId);

    FinancialTransaction credit(Account account, BigDecimal amount, String description);

    FinancialTransaction credit(Account account, BigDecimal amount, LocalDateTime transactionDate, String description);

    FinancialTransaction debit(Account account, BigDecimal amount, String description);

    FinancialTransaction debit(Account account, BigDecimal amount, LocalDateTime transactionDate, String description);
}
