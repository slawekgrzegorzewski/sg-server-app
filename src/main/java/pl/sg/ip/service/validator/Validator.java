package pl.sg.ip.service.validator;

public interface Validator {
    boolean validateDomain(int domainId);

    boolean validateDeletion();
}
