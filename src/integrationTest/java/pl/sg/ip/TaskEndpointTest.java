package pl.sg.ip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.Task;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class TaskEndpointTest extends AbstractIPBaseTest {

    private static final int SECOND_DOMAIN_ID = 2;
    public static final String TASK_CO_AUTHORS = "author1, author2, author3";
    public static final String TASK_DESCRIPTION = "Description";
    private static final List<String> TASK_ATTACHMENTS = List.of("url/to/file1", "url/to/file2", "url/to/file3");

    @Test
    void shouldFailUnauthenticatedGetTaskRequest() {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.GET,
                new HttpEntity<String>(headers(DEFAULT_DOMAIN_ID)),
                String.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedCreateTaskRequest() throws JsonProcessingException {
        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToCreate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedUpdateTaskRequest() throws JsonProcessingException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void getTaskRolesAccess(String[] roles, int expectedResponse) {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.GET,
                new HttpEntity<String>(authenticatedHeaders(DEFAULT_DOMAIN_ID, roles)),
                String.class);
        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void createTaskRolesAccess(String[] roles, int expectedResponse) throws JsonProcessingException {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, roles);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToCreate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);
        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void updateTaskRolesAccess(String[] roles, int expectedResponse) throws JsonProcessingException {

        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, roles);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @Test
    void shouldFailCreationOfTaskForIPFromOtherDomain() throws JsonProcessingException {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(SECOND_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);
        TaskData testTaskToCreate = new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldCreateTask() throws JsonProcessingException {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);
        TaskData testTaskToCreate = new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);
        assertEquals(200, response.getStatusCode().value());
        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());
        assertEquals(TASK_CO_AUTHORS, tasks.get(0).getCoAuthors());
        assertEquals(TASK_DESCRIPTION, tasks.get(0).getDescription());
    }

    @Test
    void shouldGetTasksFromDomainToWhichUserHasAccessOnlyAndFromOnlyOneIP() throws JsonProcessingException {

        var task = createTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID, TASK_DESCRIPTION, TASK_CO_AUTHORS, TASK_ATTACHMENTS);
        createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        createBasicTaskWithIntellectualProperty(SECOND_DOMAIN_ID);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(task.getIntellectualProperty().getId()),
                HttpMethod.GET,
                new HttpEntity<String>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                String.class);
        assertEquals(200, response.getStatusCode().value());

        Object body = response.getBody();

        List<pl.sg.ip.api.Task> tasks = objectMapper.readValue(Objects.requireNonNull(body).toString(), new TypeReference<>() {
        });
        assertEquals(1, tasks.size());
        assertEquals(TASK_DESCRIPTION, tasks.get(0).getDescription());
        assertEquals(TASK_CO_AUTHORS, tasks.get(0).getCoAuthors());
        assertEquals(TASK_ATTACHMENTS, tasks.get(0).getAttachments());
    }

    @Test
    void shouldFailGetForNotExistingIP() {

        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(task.getIntellectualProperty().getId() + 1),
                HttpMethod.GET,
                new HttpEntity<String>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                String.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void updateTaskRequestShouldFailWhenNoEntity() throws JsonProcessingException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromTask(task.getId() + 1),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void updateTaskRequestShouldFailWhenNoDomainAccess() throws JsonProcessingException {
        var task = createBasicTaskWithIntellectualProperty(SECOND_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void updateTaskRequestShouldUpdateTask() throws JsonProcessingException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(200, response.getStatusCode().value());
        List<Task> allTasks = taskRepository.findAll();
        assertEquals(1, allTasks.size());
        assertEquals(TASK_CO_AUTHORS, allTasks.get(0).getCoAuthors());
        assertEquals(TASK_DESCRIPTION, allTasks.get(0).getDescription());
    }

    @NotNull
    private String pathFromIntellectualProperty(int intellectualPropertyId) {
        return "http://localhost:" + serverPort + "/ipr/" + intellectualPropertyId + "/task";
    }

    @NotNull
    private String pathFromTask(int taskId) {
        return "http://localhost:" + serverPort + "/task/" + taskId;
    }

}