package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.Domain;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionsController {

    List<FinancialTransactionTO> getUserTransactions(Domain domain);

    FinancialTransactionTO transfer(Account from, Account to, @PositiveOrZero BigDecimal amount, String description);

    FinancialTransactionTO transferCashWithBankTransactions(
            Account from, Account to, @PositiveOrZero BigDecimal amount,
            String description, int firstBankTransactionId);

    FinancialTransactionTO transferWithBankTransactions(
            Account from, Account to, @PositiveOrZero BigDecimal amount,
            String description, int firstBankTransactionId, int secondBankTransactionId);

    FinancialTransactionTO transferWithConversion(Account from, Account to, @PositiveOrZero BigDecimal amount,
                                                  @PositiveOrZero BigDecimal targetAmount, @Positive BigDecimal rate,
                                                  String description);

    FinancialTransactionTO transferWithConversionWithBankTransactions(
            Account from, Account to, @PositiveOrZero BigDecimal amount, @PositiveOrZero BigDecimal targetAmount,
            @Positive BigDecimal rate, String description, int firstBankTransactionId, int secondBankTransactionId);

    FinancialTransactionTO transferCashWithConversionWithBankTransactions(
            Account from, Account to, @PositiveOrZero BigDecimal amount, @PositiveOrZero BigDecimal targetAmount,
            @Positive BigDecimal rate, String description, int firstBankTransactionId);

    FinancialTransactionTO credit(Account account, @PositiveOrZero BigDecimal amount, String description);

    FinancialTransactionTO debit(Account account, @PositiveOrZero BigDecimal amount, String description);
}
