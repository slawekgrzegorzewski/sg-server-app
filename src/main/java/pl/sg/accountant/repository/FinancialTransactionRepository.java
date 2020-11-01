package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.FinancialTransaction;

import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Integer> {
    List<FinancialTransaction> findAllByApplicationUser_LoginOrSource_ApplicationUser_LoginOrDestination_ApplicationUser_Login(String login, String login1, String login2);

    default List<FinancialTransaction> findAllByLogin(String login) {
        return this.findAllByApplicationUser_LoginOrSource_ApplicationUser_LoginOrDestination_ApplicationUser_Login(login, login, login);
    }

    ;
}
