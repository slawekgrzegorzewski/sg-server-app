package pl.sg.accountant.controller;

import pl.sg.accountant.transport.accounts.Account;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface AccountsController {

    List<Account> allAccounts();

    List<Account> domainAccount(Domain domain);

    Account createAccount(@Valid pl.sg.accountant.model.accounts.Account account);

    String updateAccount(@Valid pl.sg.accountant.model.accounts.Account account);

    String deleteAccount(pl.sg.accountant.model.accounts.Account account);
}
