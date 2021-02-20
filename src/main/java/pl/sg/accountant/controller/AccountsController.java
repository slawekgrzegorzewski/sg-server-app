package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface AccountsController {

    List<AccountTO> allAccounts();

    List<AccountTO> domainAccount(Domain domain);

    AccountTO createAccount(@Valid Account account);

    String updateAccount(@Valid Account account);

    String deleteAccount(Account account);
}
