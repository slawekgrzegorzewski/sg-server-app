package pl.sg.accountant.service.ledger;

import pl.sg.accountant.model.ledger.PerformedService;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;

public interface PerformedServicesService {

    PerformedService getOne(Integer serviceId);

    List<PerformedService> services(Domain domain, YearMonth forMonth);

    PerformedService create(PerformedService service);

    PerformedService update(PerformedService service);
}
