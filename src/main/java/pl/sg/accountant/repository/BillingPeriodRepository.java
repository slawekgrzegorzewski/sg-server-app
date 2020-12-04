package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {
    Optional<BillingPeriod> findByPeriodEqualsAndApplicationUserEquals(YearMonth month, ApplicationUser user);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms WHERE ms IS NULL AND bp.applicationUser = ?1")
    List<BillingPeriod> allUnfinishedBillingPeriods(ApplicationUser user);

    @Query("SELECT bp FROM BillingPeriod bp LEFT JOIN bp.monthSummary ms WHERE ms IS NULL AND bp.applicationUser = ?1 and bp.period = ?2")
    Optional<BillingPeriod> unfinishedBillingPeriod(ApplicationUser user, YearMonth month);

    default Optional<BillingPeriod> unfinishedCurrentBillingPeriod(ApplicationUser user) {
        return this.unfinishedBillingPeriod(user, YearMonth.now());
    }
}
