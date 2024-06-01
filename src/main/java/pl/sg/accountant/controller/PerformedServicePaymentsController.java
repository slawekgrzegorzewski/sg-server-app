package pl.sg.accountant.controller;

import pl.sg.accountant.model.ledger.PerformedServicePayment;
import pl.sg.accountant.transport.accounts.PerformedServicePaymentTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;

public interface PerformedServicePaymentsController {

    List<PerformedServicePaymentTO> payments(Domain domain, YearMonth forMonth);

    PerformedServicePaymentTO createPayment(ApplicationUser user, @Valid PerformedServicePayment client);

    PerformedServicePaymentTO updatePayment(ApplicationUser user, @Valid PerformedServicePayment client);
}
