package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.repository.TimeRecordRepository;
import pl.sg.ip.service.validator.Validator;
import pl.sg.ip.service.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeRecordJPAService implements TimeRecordService {

    private final DomainRepository domainRepository;
    private final TaskRepository taskRepository;
    private final TimeRecordRepository timeRecordRepository;
    private final ValidatorFactory validatorFactory;

    public TimeRecordJPAService(DomainRepository domainRepository, TaskRepository taskRepository, TimeRecordRepository timeRecordRepository, ValidatorFactory validatorFactory) {
        this.domainRepository = domainRepository;
        this.taskRepository = taskRepository;
        this.timeRecordRepository = timeRecordRepository;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public TimeRecord create(int domainId, TimeRecordData createData) {
        if (!validate(createData)) {
            throw new IPException("Trying to invoke illegal action during timer record creation: " + createData.getAssignmentAction());
        }
        Domain domain = domainRepository.findById(domainId).orElseThrow();
        Task task = createData.getAssignmentAction() == TimeRecordData.AssignmentAction.ASSIGN && createData.getTaskId() != null
                ? taskRepository.findById(createData.getTaskId()).orElseThrow()
                : null;
        if (task != null && !validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to create time record in task from other domain.");
        }
        TimeRecord timeRecord = timeRecordRepository.save(
                new TimeRecord(createData.getDate(), createData.getNumberOfHours(), createData.getDescription(), domain, task)
        );
        if (task != null) {
            List<TimeRecord> timeRecords = new ArrayList<>(task.getTimeRecords());
            timeRecords.add(timeRecord);
            task.setTimeRecords(timeRecords);
            taskRepository.save(task);
        }
        return timeRecord;
    }

    @Override
    public void update(int domainId, int timeRecordId, TimeRecordData updateData) {
        if (!validateCorrectConfiguration(updateData)) {
            throw new IPException("Trying to invoke illegal action during timer record update: " + updateData.getAssignmentAction());
        }
        domainRepository.findById(domainId).orElseThrow();
        Task task = updateData.getAssignmentAction() == TimeRecordData.AssignmentAction.ASSIGN && updateData.getTaskId() != null
                ? taskRepository.findById(updateData.getTaskId()).orElseThrow()
                : null;
        if (task != null && !validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to create time record in task from other domain.");
        }
        TimeRecord timeRecord = timeRecordRepository.findById(timeRecordId).orElseThrow();
        timeRecord.setDate(updateData.getDate());
        timeRecord.setDescription(updateData.getDescription());
        timeRecord.setNumberOfHours(updateData.getNumberOfHours());
        if (task != null) {
            if (timeRecord.getTask() != null) {
                removeTimeRecordFromTask(timeRecord);
            }
            addTimeRecordToTask(timeRecord, task);
        } else if (updateData.getAssignmentAction() == TimeRecordData.AssignmentAction.UNASSIGN && timeRecord.getTask() != null) {
            removeTimeRecordFromTask(timeRecord);
        }
        timeRecordRepository.save(timeRecord);
    }

    @Override
    public void delete(int domainId, int timeRecordId) {
        domainRepository.findById(domainId).orElseThrow();
        TimeRecord timeRecord = timeRecordRepository.findById(timeRecordId).orElseThrow();
        Validator validator = validatorFactory.validator(timeRecord);
        if (!validator.validateDomain(domainId)) {
            throw new ForbiddenException("Trying to create time record in task from other domain.");
        }
        if (!validator.validateDeletion()) {
            throw new IPException("This entry can not be deleted.");
        }
        if (timeRecord.getTask() != null) {
            removeTimeRecordFromTask(timeRecord);
        }
        timeRecordRepository.delete(timeRecord);
    }

    private void addTimeRecordToTask(TimeRecord timeRecord, Task task) {
        List<TimeRecord> timeRecords = new ArrayList<>(task.getTimeRecords());
        timeRecords.add(timeRecord);
        task.setTimeRecords(timeRecords);
        timeRecord.setTask(task);
        taskRepository.save(task);
    }

    private void removeTimeRecordFromTask(TimeRecord timeRecord) {
        Task currentTask = timeRecord.getTask();
        ArrayList<TimeRecord> timeRecords = new ArrayList<>(currentTask.getTimeRecords());
        timeRecords.remove(timeRecord);
        currentTask.setTimeRecords(timeRecords);
        taskRepository.save(currentTask);
        timeRecord.setTask(null);
    }

    private boolean validate(TimeRecordData createData) {
        return TimeRecordData.AssignmentAction.ACTIONS_ALLOWED_DURING_CREATION.contains(createData.getAssignmentAction()) && validateCorrectConfiguration(createData);
    }

    private static boolean validateCorrectConfiguration(TimeRecordData createData) {
        return (createData.getAssignmentAction() == TimeRecordData.AssignmentAction.NOP && createData.getTaskId() == null)
               || (createData.getAssignmentAction() == TimeRecordData.AssignmentAction.ASSIGN && createData.getTaskId() != null)
               || (createData.getAssignmentAction() == TimeRecordData.AssignmentAction.UNASSIGN && createData.getTaskId() == null);
    }
}