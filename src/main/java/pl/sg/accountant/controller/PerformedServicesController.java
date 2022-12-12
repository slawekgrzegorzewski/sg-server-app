package pl.sg.accountant.controller;

import pl.sg.accountant.transport.accounts.PerformedService;
import pl.sg.application.model.Domain;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.List;

public interface PerformedServicesController {

    List<PerformedService> services(Domain domain, YearMonth forMonth);

    PerformedService createService(@Valid pl.sg.accountant.model.accounts.PerformedService client);

    PerformedService updateService(@Valid pl.sg.accountant.model.accounts.PerformedService client);
}
