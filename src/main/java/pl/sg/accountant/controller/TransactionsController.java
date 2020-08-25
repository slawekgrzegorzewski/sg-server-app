package pl.sg.accountant.controller;

import pl.sg.accountant.service.AccountsException;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.ApplicationUser;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionsController {

    List<FinancialTransactionTO> getUserTransactions(String login);

    FinancialTransactionTO transfer(int fromId, int toId, @PositiveOrZero BigDecimal amount, String description, ApplicationUser user) throws AccountsException;

    FinancialTransactionTO transferWithConversion(int fromId, int toId, @PositiveOrZero BigDecimal amount, @PositiveOrZero BigDecimal targetAmount, @Positive BigDecimal rate, String description, ApplicationUser user) throws AccountsException;

    FinancialTransactionTO credit(int accountId, @PositiveOrZero BigDecimal amount, String description, ApplicationUser user) throws AccountsException;

    FinancialTransactionTO debit(int accountId, @PositiveOrZero BigDecimal amount, String description, ApplicationUser user) throws AccountsException;
}
