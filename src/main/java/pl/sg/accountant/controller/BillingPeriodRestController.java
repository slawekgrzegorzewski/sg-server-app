package pl.sg.accountant.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.service.BillingPeriodsService;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@RestController
@RequestMapping("/billing-periods")
public class BillingPeriodRestController implements BillingPeriodController {

    private final BillingPeriodsService billingPeriodsService;
    private final ModelMapper mapper;

    public BillingPeriodRestController(BillingPeriodsService billingPeriodsService, ModelMapper mapper) {
        this.billingPeriodsService = billingPeriodsService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodTO> currentPeriod(@RequestUser ApplicationUser user) {
        return getBilling(YearMonth.now(), user);
    }

    @Override
    @GetMapping("/{period}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodTO> periodForMonth(@PathVariable("period") YearMonth month, @RequestUser ApplicationUser user) {
        return getBilling(month, user);
    }

    @NotNull
    private ResponseEntity<BillingPeriodTO> getBilling(YearMonth month, ApplicationUser user) {
        return this.billingPeriodsService.findByPeriodAndUser(month, user)
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodTO> create(@RequestUser ApplicationUser user) {
        return createBilling(YearMonth.now(), user);
    }

    @Override
    @PutMapping("/{period}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<BillingPeriodTO> create(@PathVariable("period") YearMonth month, @RequestUser ApplicationUser user) {
        return createBilling(month, user);
    }

    @NotNull
    private ResponseEntity<BillingPeriodTO> createBilling(YearMonth month, ApplicationUser user) {
        return this.billingPeriodsService.create(month, user)
                .map(billingPeriodsService::getById)
                .map(period -> mapper.map(period, BillingPeriodTO.class))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
