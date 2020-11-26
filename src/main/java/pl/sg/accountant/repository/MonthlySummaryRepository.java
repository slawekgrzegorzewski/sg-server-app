package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.summary.MonthSummary;

public interface MonthlySummaryRepository extends JpaRepository<MonthSummary, Integer> {
}
