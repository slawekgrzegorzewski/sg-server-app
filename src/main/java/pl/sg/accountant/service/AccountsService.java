package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface AccountsService {

    Account getById(ApplicationUser user, Integer accountId);

    List<Account> getAll();

    List<Account> getForUserAndDomain(ApplicationUser userName, int domainId);

    void createAccount(ApplicationUser user, Account account);

    void update(ApplicationUser user, Account account);

    void delete(ApplicationUser user, int accountId);
}
