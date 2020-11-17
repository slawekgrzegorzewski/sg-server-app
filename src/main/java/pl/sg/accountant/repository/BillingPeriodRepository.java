package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.Optional;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {
    Optional<BillingPeriod> findByPeriodEqualsAndApplicationUserEquals(YearMonth month, ApplicationUser user);
}
