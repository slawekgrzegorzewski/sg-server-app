package pl.sg.application.security.annotations;

import pl.sg.application.DomainException;
import pl.sg.application.model.Domain;

import javax.persistence.EntityManager;

public class DomainExtractor {

    private final EntityManager entityManager;

    public DomainExtractor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Domain getDomain(String domainIdHeader) {
        if (domainIdHeader == null) {
            throw new DomainException("No domainId in headers");
        }
        final int domainId;
        try {
            domainId = Integer.parseInt(domainIdHeader);
        } catch (NumberFormatException ex) {
            throw new DomainException(domainIdHeader + " can not be parsed to Domain ID");
        }
        Domain domain = this.entityManager.find(Domain.class, domainId);
        if (domain == null) {
            throw new DomainException("There is no domain with ID " + domainIdHeader);
        }
        return domain;
    }
}
