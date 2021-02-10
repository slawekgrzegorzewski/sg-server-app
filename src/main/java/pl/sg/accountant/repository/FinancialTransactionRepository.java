package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.FinancialTransaction;

import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Integer> {
    @Query("SELECT DISTINCT ft FROM FinancialTransaction ft " +
            "LEFT JOIN ft.source s " +
            "LEFT JOIN s.applicationUser sau " +
            "LEFT JOIN sau.userLogins saul " +
            "LEFT JOIN ft.destination d " +
            "LEFT JOIN d.applicationUser dau " +
            "LEFT JOIN dau.userLogins daul " +
            "WHERE saul.login = ?1 " +
            "OR daul.login = ?1")
    List<FinancialTransaction> findAllByDomain(int domainId);
}

