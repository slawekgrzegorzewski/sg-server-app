package pl.sg.accountant.service;

import pl.sg.accountant.model.ledger.Service;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ServicesService {
    List<Service> services(Domain domain);

    Service create(Service service);

    Service update(Service service);
}
