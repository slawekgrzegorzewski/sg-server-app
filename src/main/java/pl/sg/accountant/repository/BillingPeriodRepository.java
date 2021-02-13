package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {

    Optional<BillingPeriod> findByDomainIdAndPeriod(int domainId, YearMonth month);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms LEFT JOIN bp.domain d WHERE ms IS NULL AND d = ?1")
    List<BillingPeriod> allUnfinishedBillingPeriods(Domain domain);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms LEFT JOIN bp.domain d WHERE ms IS NULL AND d = ?1 and bp.period = ?2")
    Optional<BillingPeriod> unfinishedBillingPeriod(Domain domain, YearMonth month);

    default Optional<BillingPeriod> unfinishedCurrentBillingPeriod(Domain domain) {
        return this.unfinishedBillingPeriod(domain, YearMonth.now());
    }
}
