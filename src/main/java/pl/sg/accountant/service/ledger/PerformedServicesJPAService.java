package pl.sg.accountant.service.ledger;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.ledger.PerformedService;
import pl.sg.accountant.repository.ledger.PerformedServiceRepository;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
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
    public List<PerformedService> services(Domain domain, YearMonth forMonth) {
        return performedServiceRepository.forMonth(domain, forMonth);
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
