package pl.sg.accountant.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByApplicationUser_Login(String userName);
}
