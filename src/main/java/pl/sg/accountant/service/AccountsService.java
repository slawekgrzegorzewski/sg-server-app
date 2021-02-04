package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface AccountsService {
    Optional<Account> findById(Integer id);

    Account getById(Integer id);

    List<Account> getAll();

    List<Account> getForUser(ApplicationUser userName);

    void createAccount(Account account, String userName);

    void update(Account account);

    void delete(Account account);
}
