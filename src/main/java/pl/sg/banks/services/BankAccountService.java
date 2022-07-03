package pl.sg.banks.services;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;

public interface BankAccountService {
    void assignBankAccountToAnAccount(Domain domain, BankAccount bankAccount, Account account);

    void fetchAllTransactions(Domain domain);

    void fetchAllBalances(Domain domain);
}
