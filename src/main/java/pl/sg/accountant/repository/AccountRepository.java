package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
