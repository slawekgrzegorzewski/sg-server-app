package pl.sg.application.service;

import org.springframework.stereotype.Component;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;

import java.util.Optional;

@Component
public class DomainJPAService implements DomainService {
    private final DomainRepository domainRepository;

    public DomainJPAService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Override
    public Domain getById(int domainId) {
        return domainRepository.getOne(domainId);
    }

    @Override
    public Optional<Domain> findById(int domainId) {
        return domainRepository.findById(domainId);
    }

    @Override
    public Domain create(Domain newDomain) {
        newDomain.setId(null);
        return domainRepository.save(newDomain);
    }

    @Override
    public Domain save(ApplicationUser user, Domain domain) {
        user.validateAdminDomain(domain);
        return domainRepository.save(domain);
    }
}
