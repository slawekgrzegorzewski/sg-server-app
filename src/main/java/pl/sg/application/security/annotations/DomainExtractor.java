package pl.sg.application.security.annotations;

import pl.sg.application.DomainException;
import pl.sg.application.model.Domain;

import jakarta.persistence.EntityManager;

public class DomainExtractor {

    private final EntityManager entityManager;

    public DomainExtractor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Domain getDomain(String domainId) {
        if (domainId == null) {
            throw new DomainException("No domainId");
        }
        final int id;
        try {
            id = Integer.parseInt(domainId);
        } catch (NumberFormatException ex) {
            throw new DomainException(domainId + " can not be parsed to Domain ID");
        }
        Domain domain = this.entityManager.find(Domain.class, id);
        if (domain == null) {
            throw new DomainException("There is no domain with ID " + id);
        }
        return domain;
    }
}
