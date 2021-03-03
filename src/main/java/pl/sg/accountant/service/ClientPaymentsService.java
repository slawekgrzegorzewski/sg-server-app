package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ClientPaymentsService {

    ClientPayment getOne(Integer paymentId);

    List<ClientPayment> clientPayments(Domain domain);

    ClientPayment create(ClientPayment service);

    ClientPayment update(ClientPayment service);
}
