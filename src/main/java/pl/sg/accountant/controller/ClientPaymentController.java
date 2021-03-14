package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.accountant.transport.accounts.ClientPaymentTO;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentController {

    List<ClientPaymentTO> clientPayments(Domain domain, YearMonth forMonth);

    ClientPaymentTO createClientPayment(@Valid ClientPayment clientPayment);

    ClientPaymentTO updateClientPayment(@Valid ClientPayment clientPayment);
}
