package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.Optional;

public interface BillingPeriodsService {
    BillingPeriod getById(Integer id);

    Optional<BillingPeriod> findByPeriodAndUser(YearMonth month, ApplicationUser user);

    Optional<Integer> create(YearMonth month, ApplicationUser user);
}
