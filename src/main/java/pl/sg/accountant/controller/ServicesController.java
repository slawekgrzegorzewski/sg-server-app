package pl.sg.accountant.controller;

import pl.sg.accountant.transport.accounts.Service;
import pl.sg.application.model.Domain;

import jakarta.validation.Valid;
import java.util.List;

public interface ServicesController {

    List<Service> services(Domain domain);

    Service createService(@Valid pl.sg.accountant.model.ledger.Service client);

    Service updateService(@Valid pl.sg.accountant.model.ledger.Service client);
}
