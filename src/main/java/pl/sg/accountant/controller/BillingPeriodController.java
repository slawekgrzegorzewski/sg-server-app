package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.application.model.ApplicationUser;

import java.time.YearMonth;

public interface BillingPeriodController {

    ResponseEntity<BillingPeriodTO> currentPeriod(ApplicationUser user);

    ResponseEntity<BillingPeriodTO> periodForMonth(YearMonth month, ApplicationUser user);

    ResponseEntity<BillingPeriodTO> create(ApplicationUser user);

    ResponseEntity<BillingPeriodTO> create(YearMonth month, ApplicationUser user);
}
