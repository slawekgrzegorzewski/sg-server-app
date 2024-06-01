package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.ledger.RevenueAndExpenseEntry;

public interface RevenueAndExpenseEntryRepository extends JpaRepository<RevenueAndExpenseEntry, Long> {
}
