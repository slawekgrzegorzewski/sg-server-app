package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.application.model.ApplicationUser;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {
    List<FinancialTransaction> transactionsForUserAndDomain(ApplicationUser user, int domainId);

    FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount, BigDecimal amount,
                                                        String description, ApplicationUser user);

    FinancialTransaction transferMoneyWithConversion(int fromId, int toId, BigDecimal amount, BigDecimal targetAmount, BigDecimal rate, String description, ApplicationUser user);

    FinancialTransaction credit(int accountId, BigDecimal amount, String description, ApplicationUser user);

    FinancialTransaction debit(int accountId, BigDecimal amount, String description, ApplicationUser user);

}
