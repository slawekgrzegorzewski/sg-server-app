package pl.sg.accountant.service;

import java.math.BigDecimal;
import java.util.List;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;

public interface AccountsService {
    List<Account> getAll();

    List<Account> getForUser(String userName);

    void createAccount(Account account, String userName);

    FinancialTransaction transferMoneyWithoutConversion(int sourceAccount, int destinationAccount, BigDecimal amount,
                                                        String description, String userName) throws AccountstException;
}
