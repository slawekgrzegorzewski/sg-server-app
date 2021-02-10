package pl.sg.application.service;

import pl.sg.application.model.Domain;

import java.util.Optional;

public interface DomainService {
    Domain getById(int domainId);

    Optional<Domain> findById(int domainId);
}
