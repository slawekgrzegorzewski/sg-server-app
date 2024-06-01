package pl.sg.accountant.controller;

import pl.sg.accountant.transport.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentController {

    List<ClientPayment> clientPayments(Domain domain, YearMonth forMonth);

    ClientPayment createClientPayment(@Valid pl.sg.accountant.model.ledger.ClientPayment clientPayment);

    ClientPayment updateClientPayment(@Valid pl.sg.accountant.model.ledger.ClientPayment clientPayment);
}
