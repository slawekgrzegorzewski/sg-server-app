package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.PerformedServicePayment;
import pl.sg.accountant.transport.accounts.PerformedServicePaymentTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface PerformedServicePaymentsController {

    List<PerformedServicePaymentTO> payments(Domain domain);

    PerformedServicePaymentTO createPayment(ApplicationUser user, @Valid PerformedServicePayment client);

    PerformedServicePaymentTO updatePayment(ApplicationUser user, @Valid PerformedServicePayment client);
}
