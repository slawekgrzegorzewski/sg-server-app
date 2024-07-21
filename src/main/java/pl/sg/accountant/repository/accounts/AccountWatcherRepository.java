package pl.sg.accountant.repository.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.AccountWatcher;

public interface AccountWatcherRepository extends JpaRepository<AccountWatcher, Long> {
}
