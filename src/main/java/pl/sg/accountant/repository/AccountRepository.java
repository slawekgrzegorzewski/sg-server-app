package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a JOIN a.applicationUser au JOIN au.userLogins aul WHERE aul.login = ?1")
    List<Account> findAllByApplicationUserLogin(String userName);
}
