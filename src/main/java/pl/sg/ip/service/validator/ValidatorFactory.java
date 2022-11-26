package pl.sg.ip.service.validator;

import org.springframework.stereotype.Component;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;

@Component
public class ValidatorFactory {
    public Validator validator(IntellectualProperty intellectualProperty) {
        return new IntellectualPropertyValidator(intellectualProperty);
    }

    public Validator validator(Task intellectualProperty) {
        return new TaskValidator(intellectualProperty);
    }

    public Validator validator(TimeRecord intellectualProperty) {
        return new TimeRecordValidator(intellectualProperty);
    }
}
