package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;

public interface AccountsService {

    Account getById(Integer accountId);

    List<Account> getAll();

    List<Account> getForDomain(Domain domain);

    void createAccount(Account account);

    void update(Account account);

    void delete(Account account);
}
