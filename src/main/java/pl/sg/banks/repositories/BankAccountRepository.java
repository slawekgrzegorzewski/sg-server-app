package pl.sg.banks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    BankAccount getBankAccountByExternalId(String externalId);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.domain = ?1")
    List<BankAccount> findAllBankAccounts(Domain domain);

    @Query("SELECT ba FROM BankAccount ba LEFT JOIN ba.account a WHERE a IS NULL AND ba.domain = ?1")
    List<BankAccount> findBankAccountsNotAssignedToAccount(Domain domain);
}