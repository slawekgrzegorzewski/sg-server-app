package pl.sg.ip.service.validator;

import pl.sg.ip.model.TimeRecord;

public class TimeRecordValidator implements Validator {
    private final TimeRecord timeRecord;

    TimeRecordValidator(TimeRecord timeRecord) {
        this.timeRecord = timeRecord;
    }

    @Override
    public boolean validateDomain(int domainId) {
        return domainId == timeRecord.getDomain().getId();
    }

    @Override
    public boolean validateDeletion() {
        return true;
    }
}
