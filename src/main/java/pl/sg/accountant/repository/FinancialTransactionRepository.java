package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.ledger.FinancialTransaction;
import pl.sg.application.model.Domain;

import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Integer> {
    @Query("SELECT DISTINCT ft FROM FinancialTransaction ft " +
            "LEFT JOIN ft.source s " +
            "LEFT JOIN s.domain sd " +
            "LEFT JOIN ft.destination d " +
            "LEFT JOIN d.domain dd " +
            "WHERE sd = ?1 " +
            "OR dd = ?1")
    List<FinancialTransaction> findAllByDomain(Domain domain);
}

