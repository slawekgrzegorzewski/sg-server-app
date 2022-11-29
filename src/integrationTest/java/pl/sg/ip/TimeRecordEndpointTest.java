package pl.sg.ip;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.model.Task;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"dev", "fileSystemStorage"})
@Testcontainers
public class TimeRecordEndpointTest extends AbstractIPBaseTest {

    private static final int SECOND_DOMAIN_ID = 2;
    public static final String NUMBER_OF_HOURS = "0.0";
    public static final String DESCRIPTION = "";
    public static final LocalDate NOW = LocalDate.now();

    @Test
    void shouldFailUnauthenticatedCreateTimeRecordRequest() {

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, ""), headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, task.getId()), headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedUpdateTimeRecordRequest() {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, ""), headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());

        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.UNASSIGN), headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, task.getId()), headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDeleteTimeRecordRequest() {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());

        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers(DEFAULT_DOMAIN_ID)),
                Void.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void getTimeRecordRolesAccess(String role) {

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, ""), authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(
                        new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, task.getId()),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void createTimeRecordRolesAccess(String role) {
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.GET,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void updateTimeRecordRolesAccess(String role) {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, ""), authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, task.getId()), authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());

        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.UNASSIGN), authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void deleteTimeRecordRolesAccess(String role) {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());

        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());

        response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailCreationOfTimeRecordWhenCreatingForTaskFromOtherDomain() {
        var task = taskIntellectualProperty(SECOND_DOMAIN_ID);
        TimeRecordData timeRecordToCreateData = new TimeRecordData(
                LocalDate.now(),
                NUMBER_OF_HOURS,
                "",
                TimeRecordData.AssignmentAction.ASSIGN,
                task.getId());

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailCreationOfTimeRecordWhenCreatingForNotExistingTask() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, 1);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(timeRecordToCreateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldCreateTimeRecordWithNoAssociationWithTask() {
        TimeRecordData timeRecordToCreateData = new TimeRecordData(NOW, NUMBER_OF_HOURS, DESCRIPTION);
        taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        ResponseEntity<TimeRecord> response = restTemplate.exchange(
                pathForTimeRecord(),
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
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        ResponseEntity<TimeRecord> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(
                        new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.ASSIGN, task.getId()),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                TimeRecord.class);

        assertEquals(200, response.getStatusCode().value());
        TimeRecord createdTimeRecord = response.getBody();
        assertNotNull(createdTimeRecord);
        assertEquals(NUMBER_OF_HOURS, createdTimeRecord.getNumberOfHours());
        assertEquals(DESCRIPTION, createdTimeRecord.getDescription());
        assertEquals(DEFAULT_DOMAIN_ID, createdTimeRecord.getDomain().getId());
        commitAndStartNewTransaction();
        List<Task> tasksWithTimeRecords = taskRepository.findAll().stream()
                .filter(t -> !t.getTimeRecords().isEmpty())
                .toList();
        assertEquals(1, tasksWithTimeRecords.size());
        assertEquals(1, tasksWithTimeRecords.get(0).getTimeRecords().size());
        assertEquals(createdTimeRecord.getId(), tasksWithTimeRecords.get(0).getTimeRecords().get(0).getId());
    }

    @Test
    void shouldFailUpdateForOtherDomain() {
        var timeRecord = timeRecord(SECOND_DOMAIN_ID);
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, timeRecord.getTask().getId()),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailUpdateWhenAssigningToOtherTaskFromOtherDomain() {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);
        var task = taskIntellectualProperty(SECOND_DOMAIN_ID);
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, task.getId()),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailUpdateForNotExistingTimeRecord() {
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(1),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, ""),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldFailUpdateWhenAssigningToNotExistingTask() {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TimeRecordData(LocalDate.now(), NUMBER_OF_HOURS, "", TimeRecordData.AssignmentAction.ASSIGN, 1),
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testAssignmentJourney() {
        var timeRecord = notAssignedTimeRecord(DEFAULT_DOMAIN_ID);
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        var task2 = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        testSingleStep(1, task.getId(), task2.getId(), timeRecord.getId(), Function.identity(), 0, 0);
        testSingleStep(2, task.getId(), task2.getId(), timeRecord.getId(), data -> {
            data.setAssignmentAction(TimeRecordData.AssignmentAction.ASSIGN);
            data.setTaskId(task.getId());
            return data;
        }, 1, 0);
        testSingleStep(3, task.getId(), task2.getId(), timeRecord.getId(), Function.identity(), 1, 0);
        testSingleStep(4, task.getId(), task2.getId(), timeRecord.getId(), data -> {
            data.setAssignmentAction(TimeRecordData.AssignmentAction.ASSIGN);
            data.setTaskId(task2.getId());
            return data;
        }, 0, 1);
        testSingleStep(5, task.getId(), task2.getId(), timeRecord.getId(), Function.identity(), 0, 1);
        testSingleStep(6, task.getId(), task2.getId(), timeRecord.getId(), data -> {
            data.setAssignmentAction(TimeRecordData.AssignmentAction.UNASSIGN);
            return data;
        }, 0, 0);
    }

    private void testSingleStep(int step, int taskId, int task2Id, int timeRecordId, Function<TimeRecordData, TimeRecordData> customizeUpdateData, int sizeOfTimeRecordsInTask, int sizeOfTimeRecordsInTask2) {
        TimeRecordData updateData = customizeUpdateData.apply(new TimeRecordData(LocalDate.now().plusDays(step), BigDecimal.valueOf(step).toString(), String.valueOf(step)));
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecordId),
                HttpMethod.PATCH,
                new HttpEntity<>(updateData, authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")), Void.class);

        assertEquals(200, response.getStatusCode().value());
        commitAndStartNewTransaction();
        pl.sg.ip.model.TimeRecord timeRecord = timeRecordRepository.getReferenceById(timeRecordId);
        assertEquals(LocalDate.now().plusDays(step), timeRecord.getDate());
        assertEquals(0, BigDecimal.valueOf(step).compareTo(timeRecord.getNumberOfHours()));
        assertEquals(String.valueOf(step), timeRecord.getDescription());
        assertEquals(sizeOfTimeRecordsInTask, taskRepository.getReferenceById(taskId).getTimeRecords().size());
        assertEquals(sizeOfTimeRecordsInTask2, taskRepository.getReferenceById(task2Id).getTimeRecords().size());
    }

    @ParameterizedTest
    @MethodSource("incorrectTaskCreationData")
    void shouldFailCreationWhenAssigmentActionIsNotRight(TimeRecordData timeRecordData, int expectedResponse, boolean putTaskIdInTimeRecord) {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        if (putTaskIdInTimeRecord) {
            timeRecordData.setTaskId(task.getId());
        }

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.PUT,
                new HttpEntity<>(
                        timeRecordData,
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    public static Stream<Arguments> incorrectTaskCreationData() {
        return Stream.of(
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.UNASSIGN, 1), 400, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.UNASSIGN, null), 400, false),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.NOP, 1), 400, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.NOP, null), 200, false),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.ASSIGN, 1), 200, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.ASSIGN, null), 400, false)
        );
    }

    @ParameterizedTest
    @MethodSource("incorrectTaskUpdateData")
    void shouldFailUpdateWhenAssigmentActionIsNotRight(TimeRecordData timeRecordData, int expectedResponse, boolean putTaskIdInTimeRecord) {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);
        if (putTaskIdInTimeRecord) {
            timeRecordData.setTaskId(timeRecord.getTask().getId());
        }

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(
                        timeRecordData,
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    public static Stream<Arguments> incorrectTaskUpdateData() {
        return Stream.of(
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.UNASSIGN, 1), 400, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.UNASSIGN, null), 200, false),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.NOP, 1), 400, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.NOP, null), 200, false),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.ASSIGN, 1), 200, true),
                Arguments.of(new TimeRecordData(NOW.minusDays(2), NUMBER_OF_HOURS, DESCRIPTION, TimeRecordData.AssignmentAction.ASSIGN, null), 400, false)
        );
    }

    @Test
    public void shouldGetUnassociatedTimeRecordsFromDomain() {
        timeRecord(DEFAULT_DOMAIN_ID);
        var expectedTimeRecord = notAssignedTimeRecord(DEFAULT_DOMAIN_ID);
        timeRecord(SECOND_DOMAIN_ID);
        notAssignedTimeRecord(SECOND_DOMAIN_ID);

        ResponseEntity<List<TimeRecord>> response = restTemplate.exchange(
                pathForTimeRecord(),
                HttpMethod.GET,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                new ParameterizedTypeReference<>() {
                });
        assertEquals(200, response.getStatusCode().value());
        List<TimeRecord> result = response.getBody();
        assertNotNull(result);
        assertEquals(1, response.getBody().size());
        assertEquals(expectedTimeRecord.getId(), response.getBody().get(0).getId());
    }

    @Test
    public void shouldFailDeletionForAnotherDomain() {
        var timeRecord = timeRecord(SECOND_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    public void shouldFailDeletionForNonExistingEntity() {
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(1),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void shouldDeleteTimeRecordNotAssignedToTask() {
        var timeRecord = notAssignedTimeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(200, response.getStatusCode().value());
        commitAndStartNewTransaction();
        taskRepository.findAll().forEach(task -> assertFalse(task.getTimeRecords().stream().anyMatch(tr -> tr.getId().equals(timeRecord.getId()))));
    }

    @Test
    public void shouldDeleteTimeRecordAssignedToTask() {
        var timeRecord = timeRecord(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTimeRecordUpdate(timeRecord.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);
        assertEquals(200, response.getStatusCode().value());
        commitAndStartNewTransaction();
        taskRepository.findAll().forEach(task -> assertFalse(task.getTimeRecords().stream().anyMatch(tr -> tr.getId().equals(timeRecord.getId()))));
    }


    @NotNull
    private String pathForTimeRecord() {
        return "http://localhost:%d/time-record".formatted(serverPort);
    }

    @NotNull
    private String pathForTimeRecordUpdate(int timeRecordId) {
        return "%s/%s".formatted(pathForTimeRecord(), timeRecordId);
    }
}