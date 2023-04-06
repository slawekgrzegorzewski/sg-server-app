package pl.sg.ledger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.ledger.RevenueAndExpenseEntry;

public interface RevenueAndExpenseEntryRepository extends JpaRepository<RevenueAndExpenseEntry, Long> {
}
