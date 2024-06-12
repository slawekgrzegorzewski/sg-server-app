package pl.sg.accountant.repository.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByDomain(Domain domain);

    Account findByPublicId(UUID publicId);
}
