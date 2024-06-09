package pl.sg.accountant.repository.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByDomain(Domain domain);
}
