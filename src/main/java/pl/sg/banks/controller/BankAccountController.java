package pl.sg.banks.controller;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.banks.transport.BankAccount;

import java.util.List;

public interface BankAccountController {

    List<BankAccount> getBankAccountsNotAssignedToAnyAccount(Domain domain);

    void assignBankAccountToAnAccount(Domain domain, pl.sg.banks.model.BankAccount bankAccount, Account account);

    void fetchAllTransactions(Domain domain);

    void fetchAllBalances(@RequestDomain Domain domain);
}
