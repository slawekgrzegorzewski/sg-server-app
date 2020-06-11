package pl.sg.accountant.service;

import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsService {
    List<Account> getAll();

    void createAccount(Account account);

    FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount, BigDecimal amount, String description) throws AccountstException;
}
