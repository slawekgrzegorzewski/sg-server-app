package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.summary.MonthSummary;

import java.util.List;
import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthSummary, Integer> {
    Optional<MonthSummary> findByBillingPeriod(BillingPeriod billingPeriod);
}
