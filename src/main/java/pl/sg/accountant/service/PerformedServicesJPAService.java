package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.PerformedService;
import pl.sg.accountant.repository.PerformedServiceRepository;
import pl.sg.application.model.Domain;

import java.util.List;

@Component
public class PerformedServicesJPAService implements PerformedServicesService {

    private final PerformedServiceRepository performedServiceRepository;

    public PerformedServicesJPAService(PerformedServiceRepository performedServiceRepository) {
        this.performedServiceRepository = performedServiceRepository;
    }

    @Override
    public PerformedService getOne(Integer serviceId) {
        return performedServiceRepository.getOne(serviceId);
    }

    @Override
    public List<PerformedService> services(Domain domain) {
        return performedServiceRepository.findByDomain(domain);
    }

    @Override
    public PerformedService create(PerformedService service) {
        service.setId(null);
        return this.performedServiceRepository.save(service);
    }

    @Override
    public PerformedService update(PerformedService service) {
        return this.performedServiceRepository.save(service);
    }
}
