package pl.sg.accountant.repository.billings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {

    Optional<BillingPeriod> findByDomainAndPeriod(Domain domain, YearMonth month);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms LEFT JOIN bp.domain d WHERE ms IS NULL AND d = ?1")
    List<BillingPeriod> allUnfinishedBillingPeriods(Domain domain);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms LEFT JOIN bp.domain d WHERE ms IS NULL AND d = ?1 and bp.period = ?2")
    Optional<BillingPeriod> unfinishedBillingPeriod(Domain domain, YearMonth month);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms LEFT JOIN bp.domain d WHERE ms IS NULL AND d = ?1")
    Optional<BillingPeriod> lastUnfinishedBillingPeriod(Domain domain);
}
