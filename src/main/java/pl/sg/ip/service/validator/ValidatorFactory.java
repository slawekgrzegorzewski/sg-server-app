package pl.sg.ip.service.validator;

import org.springframework.stereotype.Component;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.model.TimeRecordCategory;

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

    public Validator validator(TimeRecordCategory timeRecordCategory) {
        return new TimeRecordCategoryValidator(timeRecordCategory);
    }
}
