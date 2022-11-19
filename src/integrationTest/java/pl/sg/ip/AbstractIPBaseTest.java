package pl.sg.ip;

import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sg.AbstractContainerBaseTest;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.repository.IntellectualPropertyRepository;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.repository.TimeRecordRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AbstractIPBaseTest extends AbstractContainerBaseTest {

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
                Arguments.of(new String[]{"ACCOUNTANT_ADMIN"}, 403),
                Arguments.of(new String[]{"ACCOUNTANT_USER"}, 403),
                Arguments.of(new String[]{"CHECKER_ADMIN"}, 403),
                Arguments.of(new String[]{"CHECKER_USER"}, 403),
                Arguments.of(new String[]{"SYR_USER"}, 403),
                Arguments.of(new String[]{"SYR_ADMIN"}, 403),
                Arguments.of(new String[]{"CUBES"}, 403)
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
    protected IntellectualProperty createBasicIntellectualPropertyForDomain(int domainId) {
        return createIntellectualProperty(domainId, LocalDate.now(), LocalDate.now().plusDays(1), "");
    }

    @NotNull
    protected IntellectualProperty createIntellectualProperty(int domainId, LocalDate startDate, LocalDate endDate, String description) {
        Domain domain = this.domainRepository.getReferenceById(domainId);
        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(new IntellectualProperty(startDate, endDate, description, domain));
        commitAndStartNewTransaction();
        return intellectualProperty;
    }

    @NotNull
    protected IntellectualProperty createIntellectualPropertyWithTaskAndTimeRecords(int domainId, LocalDate startDate, LocalDate endDate, String description) {
        Domain domain = this.domainRepository.getReferenceById(domainId);

        List<TimeRecord> timeRecords = timeRecordRepository.saveAllAndFlush(List.of(
                new TimeRecord(startDate, 8, description, domain, null),
                new TimeRecord(endDate, 8, description, domain, null)));

        Task task = taskRepository.save(new Task("", "", Lists.newArrayList(), null, timeRecords));
        timeRecords.forEach(tr -> tr.setTask(task));
        timeRecordRepository.saveAllAndFlush(timeRecords);

        IntellectualProperty toCreate = new IntellectualProperty(startDate, endDate, description, domain);
        ArrayList<Task> tasks = Lists.newArrayList();
        tasks.add(task);
        toCreate.setTasks(tasks);
        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(toCreate);
        task.setIntellectualProperty(intellectualProperty);
        taskRepository.save(task);

        commitAndStartNewTransaction();
        return intellectualProperty;
    }

    @NotNull
    protected Task createBasicTaskWithIntellectualProperty(int domainId) {
        return createTaskWithIntellectualProperty(domainId, "", "", Lists.newArrayList());
    }

    @NotNull
    protected Task createTaskWithIntellectualProperty(int domainId, String description, String coAuthors, List<String> attachments) {
        Domain domain = this.domainRepository.getReferenceById(domainId);

        Task task = taskRepository.save(new Task(description, coAuthors, attachments, null, Lists.newArrayList()));

        IntellectualProperty intellectualProperty = intellectualPropertyRepository.save(new IntellectualProperty(LocalDate.now(), LocalDate.now().plusDays(1), "", domain));
        ArrayList<Task> tasks = Lists.newArrayList();
        tasks.add(task);
        intellectualProperty.setTasks(tasks);
        intellectualProperty = intellectualPropertyRepository.save(intellectualProperty);

        task.setIntellectualProperty(intellectualProperty);
        task = taskRepository.save(task);
        commitAndStartNewTransaction();
        return task;
    }
}
