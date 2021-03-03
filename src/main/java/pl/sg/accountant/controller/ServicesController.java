package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.Service;
import pl.sg.accountant.transport.accounts.ServiceTO;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface ServicesController {

    List<ServiceTO> services(Domain domain);

    ServiceTO createService(@Valid Service client);

    ServiceTO updateService(@Valid Service client);
}
