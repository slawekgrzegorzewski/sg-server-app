package pl.sg.application.service;

import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.Optional;

public interface DomainService {
    Domain getById(int domainId);

    Optional<Domain> findById(int domainId);

    Domain create(Domain newDomain);

    Domain save(ApplicationUser user, Domain newDomain);
}
