package pl.sg.accountant.service;

import pl.sg.accountant.model.ledger.PerformedServicePayment;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PerformedServicePaymentsService {
    List<PerformedServicePayment> payments(Domain domain);

    PerformedServicePayment create(ApplicationUser user, PerformedServicePayment payment);

    PerformedServicePayment update(PerformedServicePayment payment);
}
