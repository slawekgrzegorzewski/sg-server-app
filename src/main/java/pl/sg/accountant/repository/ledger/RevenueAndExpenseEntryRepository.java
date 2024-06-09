package pl.sg.accountant.repository.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.ledger.RevenueAndExpenseEntry;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface RevenueAndExpenseEntryRepository extends JpaRepository<RevenueAndExpenseEntry, Long> {

    default List<RevenueAndExpenseEntry> findRevenueAndExpenseEntriesInMonth(int domainId, YearMonth yearMonth) {
        return findRevenueAndExpenseEntriesByDomain_IdAndEntryDateBetween(domainId, yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    List<RevenueAndExpenseEntry> findRevenueAndExpenseEntriesByDomain_IdAndEntryDateBetween(int domainId, LocalDate from, LocalDate to);

    RevenueAndExpenseEntry getRevenueAndExpenseEntriesByPublicId(UUID publicId);
}
