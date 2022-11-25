package pl.sg.ip.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.repository.IntellectualPropertyRepository;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.repository.TimeRecordRepository;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class TimeRecordJPAService implements TimeRecordService {

    private final DomainRepository domainRepository;
    private final TaskRepository taskRepository;
    private final IntellectualPropertyRepository intellectualPropertyRepository;
    private final TimeRecordRepository timeRecordRepository;

    public TimeRecordJPAService(DomainRepository domainRepository, TaskRepository taskRepository, IntellectualPropertyRepository intellectualPropertyRepository, TimeRecordRepository timeRecordRepository) {
        this.domainRepository = domainRepository;
        this.taskRepository = taskRepository;
        this.intellectualPropertyRepository = intellectualPropertyRepository;
        this.timeRecordRepository = timeRecordRepository;
    }

    public TimeRecord createWithoutTask(int domainId, TimeRecordData createData) {
        return createTimeRecord(domainId, null, createData);
    }

    public TimeRecord createInTask(int domainId, int taskId, TimeRecordData createData) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        return createTimeRecord(domainId, task, createData);
    }

    private TimeRecord createTimeRecord(int domainId, @Nullable Task task, TimeRecordData createData) {
        Domain domain = domainRepository.findById(domainId).orElseThrow();
        if (task != null && !new IPValidator(task.getIntellectualProperty()).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to create time record in task from other domain.");
        }
        TimeRecord timeRecord = timeRecordRepository.save(
                new TimeRecord(createData.getDate(), createData.getNumberOfHours(), createData.getDescription(), domain, task)
        );
        ofNullable(task).ifPresent(t -> {
            List<TimeRecord> timeRecords = new ArrayList<>(t.getTimeRecords());
            timeRecords.add(timeRecord);
            task.setTimeRecords(timeRecords);
            taskRepository.save(task);
        });
        return timeRecord;
    }
}