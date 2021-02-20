package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {
    List<FinancialTransaction> transactionsForDomain(Domain domain);

    FinancialTransaction transferMoneyWithoutConversion(Account source, Account destination, BigDecimal amount,
                                                        String description);

    FinancialTransaction transferMoneyWithConversion(Account source, Account destination, BigDecimal amount, BigDecimal targetAmount,
                                                     BigDecimal rate, String description);

    FinancialTransaction credit(Account account, BigDecimal amount, String description);

    FinancialTransaction debit(Account account, BigDecimal amount, String description);

}
