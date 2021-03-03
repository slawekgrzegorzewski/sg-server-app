package pl.sg.accountant.controller;

import pl.sg.accountant.model.accounts.PerformedService;
import pl.sg.accountant.transport.accounts.PerformedServiceTO;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.util.List;

public interface PerformedServicesController {

    List<PerformedServiceTO> services(Domain domain);

    PerformedServiceTO createService(@Valid PerformedService client);

    PerformedServiceTO updateService(@Valid PerformedService client);
}
