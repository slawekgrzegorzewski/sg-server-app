package pl.sg.accountant.controller;

import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.application.model.ApplicationUser;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public interface TransactionsController {

    List<FinancialTransactionTO> getUserTransactions(ApplicationUser user, int domainId);

    FinancialTransactionTO transfer(ApplicationUser user, int fromId, int toId, @PositiveOrZero BigDecimal amount, String description) ;

    FinancialTransactionTO transferWithConversion(ApplicationUser user, int fromId, int toId, @PositiveOrZero BigDecimal amount, @PositiveOrZero BigDecimal targetAmount, @Positive BigDecimal rate, String description) ;

    FinancialTransactionTO credit(ApplicationUser user, int accountId, @PositiveOrZero BigDecimal amount, String description) ;

    FinancialTransactionTO debit(ApplicationUser user, int accountId, @PositiveOrZero BigDecimal amount, String description) ;
}
