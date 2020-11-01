package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.repository.BillingPeriodRepository;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;
import java.util.Optional;

@Component
public class BillingPeriodsJPAService implements BillingPeriodsService {

    private final BillingPeriodRepository billingPeriodRepository;

    public BillingPeriodsJPAService(BillingPeriodRepository billingPeriodRepository) {
        this.billingPeriodRepository = billingPeriodRepository;
    }

    @Override
    public BillingPeriod getById(Integer id) {
        return billingPeriodRepository.getOne(id);
    }

    @Override
    public Optional<BillingPeriod> findByPeriodAndUser(YearMonth month, ApplicationUser user) {
        return this.billingPeriodRepository.findByPeriodEqualsAndApplicationUserEquals(month, user);
    }

    @Override
    public Optional<Integer> create(YearMonth month, ApplicationUser user) {
        if (this.billingPeriodRepository.findByPeriodEqualsAndApplicationUserEquals(month, user).isPresent()) {
            return Optional.empty();
        }
        BillingPeriod period = new BillingPeriod()
                .setPeriod(month)
                .setName(month.toString())
                .setApplicationUser(user);
        billingPeriodRepository.save(period);
        return Optional.of(period.getId());
    }
}
