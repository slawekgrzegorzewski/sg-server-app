package pl.sg.banks.controller;

import org.springframework.web.bind.annotation.PostMapping;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.transport.BankAccountTO;

import java.util.List;

public interface BankAccountController {

    List<BankAccountTO> getBankAccountsNotAssignedToAnyAccount(Domain domain);

    void assignBankAccountToAnAccount(Domain domain, BankAccount bankAccount, Account account);

    void fetchAllTransactions(Domain domain);

    void fetchAllBalances(@RequestDomain Domain domain);
}
