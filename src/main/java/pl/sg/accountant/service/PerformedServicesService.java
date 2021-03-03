package pl.sg.accountant.service;

import pl.sg.accountant.model.accounts.PerformedService;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PerformedServicesService {

    PerformedService getOne(Integer serviceId);

    List<PerformedService> services(Domain domain);

    PerformedService create(PerformedService service);

    PerformedService update(PerformedService service);
}
