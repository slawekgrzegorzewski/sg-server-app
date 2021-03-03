package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.Service;
import pl.sg.accountant.repository.ServiceRepository;
import pl.sg.application.model.Domain;

import java.util.List;

@Component
public class ServicesJPAService implements ServicesService {

    private final ServiceRepository serviceRepository;

    public ServicesJPAService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Service> services(Domain domain) {
        return serviceRepository.findByDomain(domain);
    }

    @Override
    public Service create(Service service) {
        service.setId(null);
        return this.serviceRepository.save(service);
    }

    @Override
    public Service update(Service service) {
        return this.serviceRepository.save(service);
    }
}
