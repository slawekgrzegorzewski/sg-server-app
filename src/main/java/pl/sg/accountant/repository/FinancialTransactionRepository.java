package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.FinancialTransaction;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Integer> {
}
