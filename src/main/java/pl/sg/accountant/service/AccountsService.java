package pl.sg.accountant.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import pl.sg.accountant.model.Account;
import pl.sg.accountant.model.FinancialTransaction;

public interface AccountsService {
    Optional<Account> findById(Integer id);
    List<Account> getAll();

    List<Account> getForUser(String userName);

    void createAccount(Account account, String userName);

    void update(Account account);

    void delete(Account account);
}
