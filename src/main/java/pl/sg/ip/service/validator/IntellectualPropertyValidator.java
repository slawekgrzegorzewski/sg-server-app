package pl.sg.ip.service.validator;

import pl.sg.ip.model.IntellectualProperty;

import java.util.List;

import static java.util.Optional.ofNullable;

public class IntellectualPropertyValidator implements Validator {
    private final IntellectualProperty intellectualProperty;

    IntellectualPropertyValidator(IntellectualProperty intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
    }

    @Override
    public boolean validateDomain(int domainId) {
        return domainId == intellectualProperty.getDomain().getId();
    }

    @Override
    public boolean validateDeletion() {
        return ofNullable(intellectualProperty.tasks()).map(List::isEmpty).orElse(true);
    }
}
