package pl.sg.accountant.service.accounts;

import pl.sg.accountant.model.accounts.Account;

import java.util.List;

public interface AccountsService {

    Account getById(Integer accountId);

    List<Account> getAll();

    List<Account> getForDomain(long domainId);

    void createAccount(Account account);

    void update(Account account);

    void delete(Account account);
}
