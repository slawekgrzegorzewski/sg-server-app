package pl.sg.ip;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.Task;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"dev", "fileSystemStorage"})
@Testcontainers
public class TimeRecordEndpointTest extends AbstractIPBaseTest {

    private static final int SECOND_DOMAIN_ID = 2;
    public static final int NUMBER_OF_HOURS = 0;
    public static final String DESCRIPTION = "";
    public static final LocalDate NOW = LocalDate.now();

    @Test
    void shouldFailUnauthenticatedCreateTimeRecordRequest() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(LocalDate.now(), 0, "");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForTimeRecordCreation(),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, headers(DEFAULT_DOMAIN_ID)),
                String.class);
        assertEquals(401, response.getStatusCode().value());

        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordCreationForTask(task.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, headers(DEFAULT_DOMAIN_ID)),
                String.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void createTimeRecordRolesAccess(String role) {

        TimeRecordData timeRecordToCreateData = new TimeRecordData(LocalDate.now(), 0, "");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForTimeRecordCreation(),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                String.class);
        assertEquals(403, response.getStatusCode().value());

        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordCreationForTask(task.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }


    @Test
    void shouldFailCreationOfTimeRecordWhenCreatingForTaskFromOtherDomain() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(LocalDate.now(), 0, "");
        var task = createBasicTaskWithIntellectualProperty(SECOND_DOMAIN_ID);
        ResponseEntity<?> response = restTemplate.exchange(
                pathForTimeRecordCreationForTask(task.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailCreationOfTimeRecordWhenCreatingForNotExistingTask() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(LocalDate.now(), 0, "");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForTimeRecordCreationForTask(1),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                String.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldCreateTimeRecordWithNoAssociationWithTask() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(NOW, NUMBER_OF_HOURS, DESCRIPTION);
        createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        ResponseEntity<TimeRecord> response = restTemplate.exchange(
                pathForTimeRecordCreation(),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                TimeRecord.class);
        assertEquals(200, response.getStatusCode().value());
        TimeRecord createdTimeRecord = response.getBody();
        assertNotNull(createdTimeRecord);
        assertEquals(NUMBER_OF_HOURS, createdTimeRecord.getNumberOfHours());
        assertEquals(DESCRIPTION, createdTimeRecord.getDescription());
        assertEquals(NOW, createdTimeRecord.getDate());
        assertEquals(DEFAULT_DOMAIN_ID, createdTimeRecord.getDomain().getId());
        commitAndStartNewTransaction();
        assertTrue(taskRepository.findAll().stream().allMatch(t -> t.getTimeRecords().isEmpty()));
    }

    @Test
    void shouldCreateTimeRecordAssociatedWithTask() {
        var task = createBasicTaskWithIntellectualPropertyForDates(DEFAULT_DOMAIN_ID, NOW.minusDays(4), NOW.minusDays(1));
        pl.sg.ip.model.TimeRecord timeRecord = testTimeRecordCreation(task, new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION));
        assertEquals(NOW.minusDays(4), timeRecord.getTask().getIntellectualProperty().getStartDate());
        assertEquals(NOW.minusDays(1), timeRecord.getTask().getIntellectualProperty().getEndDate());
    }

    @Test
    void shouldCreateTimeRecordAssociatedWithTaskAndAlignIPStart() {
        var task = createBasicTaskWithIntellectualPropertyForDates(DEFAULT_DOMAIN_ID, NOW.minusDays(4), NOW.minusDays(1));
        TimeRecordData timeRecordToCreateData = new TimeRecordData(NOW.minusDays(5), NUMBER_OF_HOURS, DESCRIPTION);
        pl.sg.ip.model.TimeRecord timeRecord = testTimeRecordCreation(task, timeRecordToCreateData);
        assertEquals(NOW.minusDays(5), timeRecord.getTask().getIntellectualProperty().getStartDate());
        assertEquals(NOW.minusDays(1), timeRecord.getTask().getIntellectualProperty().getEndDate());
    }

    @Test
    void shouldCreateTimeRecordAssociatedWithTaskAndAlignIPEndDate() {
        var task = createBasicTaskWithIntellectualPropertyForDates(DEFAULT_DOMAIN_ID, NOW.minusDays(4), NOW.minusDays(1));
        TimeRecordData timeRecordToCreateData = new TimeRecordData(NOW, NUMBER_OF_HOURS, DESCRIPTION);
        pl.sg.ip.model.TimeRecord timeRecord = testTimeRecordCreation(task, timeRecordToCreateData);
        assertEquals(NOW.minusDays(4), timeRecord.getTask().getIntellectualProperty().getStartDate());
        assertEquals(NOW, timeRecord.getTask().getIntellectualProperty().getEndDate());
    }

    private pl.sg.ip.model.TimeRecord testTimeRecordCreation(Task task, TimeRecordData timeRecordToCreateData) {
        ResponseEntity<TimeRecord> response = restTemplate.exchange(
                pathForTimeRecordCreationForTask(task.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                TimeRecord.class);
        assertEquals(200, response.getStatusCode().value());
        TimeRecord createdTimeRecord = response.getBody();
        assertNotNull(createdTimeRecord);
        assertEquals(NUMBER_OF_HOURS, createdTimeRecord.getNumberOfHours());
        assertEquals(DESCRIPTION, createdTimeRecord.getDescription());
        assertEquals(timeRecordToCreateData.getDate(), createdTimeRecord.getDate());
        assertEquals(DEFAULT_DOMAIN_ID, createdTimeRecord.getDomain().getId());
        commitAndStartNewTransaction();
        List<Task> tasksWithTimeRecords = taskRepository.findAll().stream()
                .filter(t -> !t.getTimeRecords().isEmpty())
                .toList();
        assertEquals(1, tasksWithTimeRecords.size());
        assertEquals(1, tasksWithTimeRecords.get(0).getTimeRecords().size());
        assertEquals(createdTimeRecord.getId(), tasksWithTimeRecords.get(0).getTimeRecords().get(0).getId());
        return timeRecordRepository.getReferenceById(createdTimeRecord.getId());
    }

    @NotNull
    private String pathForTimeRecordCreation() {
        return "http://localhost:%d/time-record".formatted(serverPort);
    }

    @NotNull
    private String pathForTimeRecordCreationForTask(int taskId) {
        return "%s/%d".formatted(pathForTimeRecordCreation(), taskId);
    }
}