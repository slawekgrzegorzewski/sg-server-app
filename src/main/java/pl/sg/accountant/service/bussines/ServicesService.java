package pl.sg.accountant.service.bussines;

import pl.sg.accountant.model.bussines.Service;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ServicesService {
    List<Service> services(Domain domain);

    Service create(Service service);

    Service update(Service service);
}
