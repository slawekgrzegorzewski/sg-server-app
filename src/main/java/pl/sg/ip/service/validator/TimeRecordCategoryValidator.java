package pl.sg.ip.service.validator;

import pl.sg.ip.model.TimeRecordCategory;

public class TimeRecordCategoryValidator implements Validator {
    private final TimeRecordCategory timeRecordCategory;

    TimeRecordCategoryValidator(TimeRecordCategory timeRecordCategory) {
        this.timeRecordCategory = timeRecordCategory;
    }

    @Override
    public boolean validateDomain(int domainId) {
        return domainId == timeRecordCategory.getDomain().getId();
    }

    @Override
    public boolean validateDeletion() {
        return !timeRecordCategory.isInUse();
    }
}
