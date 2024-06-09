package pl.sg.application;

import pl.sg.application.model.Domain;
import pl.sg.loans.utils.LoansException;

import javax.money.CurrencyUnit;

public class DomainValidator {

    public static void validateDomain(int domainId, Domain otherDomain) {
        validateDomain(domainId, otherDomain.getId());
    }

    public static void validateDomain(Domain domain, Domain otherDomain) {
        validateDomain(domain.getId(), otherDomain.getId());
    }

    public static void validateDomain(int domainId, int otherDomainId) {
        if (domainId != otherDomainId) {
            throw new LoansException("Domains doesn't match " + domainId + ":" + otherDomainId);
        }
    }
}
