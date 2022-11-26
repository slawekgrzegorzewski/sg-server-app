package pl.sg.ip.service.validator;

import pl.sg.ip.model.Task;

import java.util.List;

import static java.util.Optional.ofNullable;

public class TaskValidator implements Validator {
    private final Task task;

    TaskValidator(Task task) {
        this.task = task;
    }

    @Override
    public boolean validateDomain(int domainId) {
        return domainId == task.getIntellectualProperty().getDomain().getId();
    }

    @Override
    public boolean validateDeletion() {
        return ofNullable(task.getTimeRecords()).map(List::isEmpty).orElse(true);
    }
}
