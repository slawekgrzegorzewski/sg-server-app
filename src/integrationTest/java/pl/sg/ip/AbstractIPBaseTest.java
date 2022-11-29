package pl.sg.ip;

import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sg.AbstractApplicationBaseTest;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.repository.IntellectualPropertyRepository;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.repository.TimeRecordRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractIPBaseTest extends AbstractApplicationBaseTest {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    protected IntellectualPropertyRepository intellectualPropertyRepository;
    @Autowired
    protected TaskRepository taskRepository;
    @Autowired
    protected TimeRecordRepository timeRecordRepository;

    protected static Stream<Arguments> forbiddenRolesAndResponses() {
        return Stream.of(
                Arguments.of("ACCOUNTANT_ADMIN"),
                Arguments.of("ACCOUNTANT_USER"),
                Arguments.of("CHECKER_ADMIN"),
                Arguments.of("CHECKER_USER"),
                Arguments.of("SYR_USER"),
                Arguments.of("SYR_ADMIN"),
                Arguments.of("CUBES")
        );
    }

    @AfterEach
    public void clear() {
        rollbackAndStartTransaction();
        timeRecordRepository.deleteAll();
        taskRepository.deleteAll();
        intellectualPropertyRepository.deleteAll();
        commitAndStartNewTransaction();
    }

    @NotNull
    protected IntellectualProperty intellectualProperty(int domainId) {
        return intellectualProperty(domainId, "");
    }

    @NotNull
    protected IntellectualProperty intellectualProperty(int domainId, String description) {
        Domain domain = this.domainRepository.getReferenceById(domainId);
        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(new IntellectualProperty(description, domain));
        commitAndStartNewTransaction();
        return intellectualProperty;
    }

    @NotNull
    protected IntellectualProperty intellectualPropertyTaskTimeRecords(int domainId, LocalDate... timeRecordDates) {
        return intellectualPropertyTaskTimeRecords(domainId, "", timeRecordDates);
    }

    @NotNull
    protected IntellectualProperty intellectualPropertyTaskTimeRecords(int domainId, String ipDescription, LocalDate... timeRecordDates) {
        Domain domain = this.domainRepository.getReferenceById(domainId);
        List<TimeRecord> timeRecords = timeRecordRepository.saveAllAndFlush(
                Arrays.stream(timeRecordDates)
                        .map(date -> new TimeRecord(date, BigDecimal.valueOf(8), "", domain, null))
                        .collect(Collectors.toList())
        );

        Task task = taskRepository.save(new Task("", "", Lists.newArrayList(), null, timeRecords));
        timeRecords.forEach(tr -> tr.setTask(task));
        timeRecordRepository.saveAllAndFlush(timeRecords);

        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(new IntellectualProperty(ipDescription, domain));
        task.setIntellectualProperty(intellectualProperty);

        ArrayList<Task> tasks = Lists.newArrayList();
        tasks.add(task);
        intellectualProperty.setTasks(tasks);
        taskRepository.save(task);

        commitAndStartNewTransaction();
        return intellectualProperty;
    }

    @NotNull
    protected Task taskIntellectualProperty(int domainId) {
        return taskIntellectualProperty(domainId, Lists.newArrayList());
    }

    @NotNull
    protected Task taskIntellectualProperty(int domainId, List<String> attachments) {
        return taskIntellectualProperty(domainId, "", "", attachments);
    }

    @NotNull
    protected Task taskIntellectualProperty(int domainId, String taskDescription, String taskCoAuthors, List<String> taskAttachments) {
        Domain domain = this.domainRepository.getReferenceById(domainId);

        Task task = taskRepository.save(new Task(taskDescription, taskCoAuthors, taskAttachments, null, Lists.newArrayList()));

        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(new IntellectualProperty("", domain));
        ArrayList<Task> tasks = Lists.newArrayList();
        tasks.add(task);
        intellectualProperty.setTasks(tasks);
        intellectualProperty = intellectualPropertyRepository.save(intellectualProperty);

        task.setIntellectualProperty(intellectualProperty);
        task = taskRepository.save(task);
        commitAndStartNewTransaction();
        return task;
    }

    @NotNull
    protected TimeRecord timeRecord(int domainId) {
        IntellectualProperty intellectualProperty = intellectualPropertyTaskTimeRecords(domainId, "", LocalDate.now());
        return intellectualProperty.tasks().get(0).getTimeRecords().get(0);
    }

    @NotNull
    protected TimeRecord notAssignedTimeRecord(int domainId) {
        TimeRecord timeRecord = timeRecordRepository.saveAndFlush(
                new TimeRecord(
                        LocalDate.now(),
                        BigDecimal.valueOf(8),
                        "",
                        this.domainRepository.getReferenceById(domainId),
                        null)
        );
        commitAndStartNewTransaction();
        return timeRecord;
    }

}
