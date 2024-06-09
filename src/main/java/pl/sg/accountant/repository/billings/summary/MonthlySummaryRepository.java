package pl.sg.accountant.repository.billings.summary;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthSummary, Integer> {

    MonthSummary getByBillingPeriod(BillingPeriod billingPeriod);

    Optional<MonthSummary> findByBillingPeriod(BillingPeriod billingPeriod);

    List<MonthSummary> findAllByBillingPeriod_Domain(Domain domain);
}
