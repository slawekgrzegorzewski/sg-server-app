package pl.sg.ip;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.hash.Funnels;
import org.testcontainers.shaded.com.google.common.hash.HashCode;
import org.testcontainers.shaded.com.google.common.hash.Hasher;
import org.testcontainers.shaded.com.google.common.hash.Hashing;
import org.testcontainers.shaded.com.google.common.io.ByteStreams;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.Task;
import pl.sg.ip.service.attachments.TaskAttachmentStorageService;
import pl.sg.ip.service.attachments.utils.FileSystemStorageTestUtil;
import pl.sg.ip.service.attachments.utils.StorageTestUtil;
import pl.sg.ip.service.attachments.utils.StorageTestUtilFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"dev", "fileSystemStorage"})
@Testcontainers
public class TaskEndpointTest extends AbstractIPBaseTest {

    private static final int SECOND_DOMAIN_ID = 2;

    public static final String TASK_CO_AUTHORS = "author1, author2, author3";
    public static final String TASK_DESCRIPTION = "Description";
    private static final List<String> TASK_ATTACHMENTS = List.of("url/to/file1", "url/to/file2", "url/to/file3");

    public static final String FILE_NAME = "first.pdf";
    public static final String FIRST_ATTACHMENT_IN_RESOURCE = "/pl/sg/ip/attachment1.pdf";
    public static final String SECOND_ATTACHMENT_IN_RESOURCE = "/pl/sg/ip/attachment2.pdf";
    public static final String FIRST_ATTACHMENT_FILE_NAME_IN_RESOURCE = "attachment1.pdf";
    public static final LocalDate NOW = LocalDate.now();

    @Autowired
    private StorageTestUtilFactory storageTestUtilFactory;
    @Autowired
    private TaskAttachmentStorageService taskAttachmentStorageService;
    private StorageTestUtil storageTestUtil;

    @BeforeEach
    public void beforeEach() {
        storageTestUtil = storageTestUtilFactory.create(-1, -1);
    }

    @AfterEach
    public void afterEach() throws IOException {
        if (storageTestUtil != null) {
            storageTestUtil.clearStorage();
        }
    }

    @Test
    void shouldFailUnauthenticatedCreateTaskRequest() {
        var intellectualProperty = intellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToCreate = new TaskData("", "");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                new HttpEntity<>(testTaskToCreate, headers),
                Void.class);
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedUpdateTaskRequest() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TaskData("", ""), headers),
                Void.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDeleteTaskRequest() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedUploadAttachmentRequest() throws URISyntaxException {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                headers(DEFAULT_DOMAIN_ID),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                Void.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDownloadAttachmentRequest() throws URISyntaxException, IOException {

        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDeleteAttachmentRequest() throws URISyntaxException, IOException {
        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(headers(DEFAULT_DOMAIN_ID)),
                Void.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void createTaskRolesAccess(String role) {
        var intellectualProperty = intellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                new HttpEntity<>(new TaskData("", ""), headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void updateTaskRolesAccess(String role) {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(new TaskData("", ""), headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void deleteTaskRolesAccess(String role) {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void uploadAttachmentRequestRolesAccess(String role) throws URISyntaxException {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, role),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void downloadAttachmentRequestRolesAccess(String role) throws URISyntaxException, IOException {
        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), FILE_NAME, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{role})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }


    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void deleteAttachmentRequestRolesAccess(String role) throws URISyntaxException, IOException {
        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailCreationOfTaskForIPFromOtherDomain() {
        var intellectualProperty = intellectualProperty(SECOND_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                new HttpEntity<>(
                        new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION),
                        headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldCreateTask() {
        var intellectualProperty = intellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                new HttpEntity<>(
                        new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION),
                        headers),
                Void.class);
        assertEquals(200, response.getStatusCode().value());
        List<Task> tasks = taskRepository.findAll();
        assertEquals(1, tasks.size());
        assertEquals(TASK_CO_AUTHORS, tasks.get(0).getCoAuthors());
        assertEquals(TASK_DESCRIPTION, tasks.get(0).getDescription());
    }

    @Test
    void shouldFailDeletionOfTaskForIPFromOtherDomain() {
        var task = taskIntellectualProperty(SECOND_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailDeletionOfNonExistingTask() {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(1),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldFailDeletionOfTaskWithTimeRecord() {
        var intellectualProperty = intellectualPropertyTaskTimeRecords(DEFAULT_DOMAIN_ID, NOW);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(intellectualProperty.tasks().get(0).getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void shouldDeleteTask() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(200, response.getStatusCode().value());
        commitAndStartNewTransaction();
        assertFalse(taskRepository.existsById(task.getId()));
    }

    @Test
    void updateTaskRequestShouldFailWhenNoEntity() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId() + 1),
                HttpMethod.PATCH,
                new HttpEntity<>(
                        new TaskData("", ""),
                        headers),
                Void.class);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void updateTaskRequestShouldFailWhenNoDomainAccess() {
        var task = taskIntellectualProperty(SECOND_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(
                        new TaskData("", ""),
                        headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void updateTaskRequestShouldUpdateTask() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                new HttpEntity<>(
                        new TaskData(TASK_CO_AUTHORS, TASK_DESCRIPTION),
                        headers),
                Void.class);

        assertEquals(200, response.getStatusCode().value());
        List<Task> allTasks = taskRepository.findAll();
        assertEquals(1, allTasks.size());
        assertEquals(TASK_CO_AUTHORS, allTasks.get(0).getCoAuthors());
        assertEquals(TASK_DESCRIPTION, allTasks.get(0).getDescription());
    }

    @Test
    void shouldFailUploadAttachmentRequestForNonExistingTask() throws URISyntaxException {

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachment(1),
                HttpMethod.POST,
                prepareUploadForm(
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                        FILE_NAME,
                        FIRST_ATTACHMENT_IN_RESOURCE),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldFailUploadAttachmentRequestForTaskFromOtherDomain() throws URISyntaxException {
        var task = taskIntellectualProperty(SECOND_DOMAIN_ID);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                FILE_NAME,
                FIRST_ATTACHMENT_IN_RESOURCE);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldUploadAttachmentRequest() throws URISyntaxException, IOException {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                prepareUploadForm(
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                        FILE_NAME,
                        FIRST_ATTACHMENT_IN_RESOURCE),
                Void.class);

        task = taskRepository.getReferenceById(task.getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, task.getAttachments().size());
        assertEquals(FILE_NAME, task.getAttachments().get(0));

        try (InputStream inputStream = taskAttachmentStorageService.getFile(task.getIntellectualProperty().getId(), task.getId(), FILE_NAME)
                .orElseThrow(() -> new AssertionFailedError("expected a file here"))) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(FIRST_ATTACHMENT_FILE_NAME_IN_RESOURCE), hash.toString());
        }
    }

    @Test
    void shouldKeepOriginalFileWhenReuploadAttachmentRequest() throws URISyntaxException, IOException {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                prepareUploadForm(
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                        FILE_NAME,
                        FIRST_ATTACHMENT_IN_RESOURCE),
                Void.class);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                prepareUploadForm(
                        authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                        FILE_NAME,
                        SECOND_ATTACHMENT_IN_RESOURCE),
                String.class);

        assertEquals(400, response.getStatusCode().value());

        try (InputStream inputStream = taskAttachmentStorageService
                .getFile(task.getIntellectualProperty().getId(), task.getId(), FILE_NAME)
                .orElseThrow(() -> new AssertionFailedError("expected a file here"))) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(FIRST_ATTACHMENT_FILE_NAME_IN_RESOURCE), hash.toString());
        }
    }

    @Test
    void shouldFailDownloadingAttachmentForTaskInOtherDomain() throws URISyntaxException, IOException {
        var task = createTaskWithAttachment(SECOND_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), FILE_NAME, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailDownloadingForNonExistingAttachmentAndUpdateTaskIfThisAttachmentWasThere() {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        task.setAttachments(List.of(FILE_NAME));
        taskRepository.save(task);
        commitAndStartNewTransaction();

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), FILE_NAME, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
        commitAndStartNewTransaction();
        task = taskRepository.getReferenceById(task.getId());
        assertTrue(task.getAttachments().isEmpty());
    }

    @Test
    void shouldFailDownloadingForAttachmentNotRelatedInTaskAndRemoveItFromStorage() throws IOException, URISyntaxException {
        var task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage("/pl/sg/ip/attachment1.pdf", FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), FILE_NAME, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
        assertFalse(storageTestUtil.checkFileExistenceInStorage(FILE_NAME));
    }

    @Test
    void shouldDownloadAttachment() throws IOException, URISyntaxException {
        var task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Resource> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), FILE_NAME, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(200, response.getStatusCode().value());

        try (InputStream inputStream = Objects.requireNonNull(response.getBody()).getInputStream()) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(FIRST_ATTACHMENT_FILE_NAME_IN_RESOURCE), hash.toString());
        }
    }

    @Test
    void shouldFailDeleteAttachmentRequestBelongingToOtherDomain() throws URISyntaxException, IOException {
        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(SECOND_DOMAIN_ID, "IPR")),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailDeleteAttachmentRequestForNonExistingAttachment() {
        Task task = taskIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldDeleteAttachment() throws IOException, URISyntaxException {
        Task task = createTaskWithAttachment(DEFAULT_DOMAIN_ID, FILE_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);

        assertEquals(200, response.getStatusCode().value());
        assertFalse(storageTestUtil.checkFileExistenceInStorage(FILE_NAME));
        commitAndStartNewTransaction();
        task = taskRepository.getReferenceById(task.getId());
        assertFalse(task.getAttachments().contains(FILE_NAME));
    }

    @Test
    void shouldDeleteReferencedButNotPresentInStorageAttachment() {
        Task task = taskIntellectualProperty(DEFAULT_DOMAIN_ID, List.of(FILE_NAME));

        ResponseEntity<Void> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), FILE_NAME),
                HttpMethod.DELETE,
                new HttpEntity<>(authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR")),
                Void.class);

        assertEquals(299, response.getStatusCodeValue());
    }

    @NotNull
    private String pathFromIntellectualProperty(int intellectualPropertyId) {
        return "http://localhost:%d/ipr/%d".formatted(serverPort, intellectualPropertyId);
    }

    @NotNull
    private String pathForTask(int taskId) {
        return "http://localhost:%d/task/%d".formatted(serverPort, taskId);
    }

    @NotNull
    private String pathForAttachment(int taskId) {
        return "http://localhost:%d/task/%d/attachment".formatted(serverPort, taskId);
    }

    @NotNull
    private String pathForAttachmentDownload(int taskId, String fileName) {
        return "%s/%s".formatted(pathForAttachment(taskId),
                URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @NotNull
    private String pathForAttachmentDownloadWithAuthentication(int taskId, String fileName, int domainId, String token) {
        return "%s?domainId=%s&authorization=%s".formatted(
                pathForAttachmentDownload(taskId, URLEncoder.encode(fileName, StandardCharsets.UTF_8)),
                String.valueOf(domainId),
                URLEncoder.encode(token, StandardCharsets.UTF_8));
    }

    @NotNull
    private HttpEntity<LinkedMultiValueMap<String, Object>> prepareUploadForm(HttpHeaders headers, String fileName, String resourceName) throws URISyntaxException {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        var form = new LinkedMultiValueMap<String, Object>();
        form.add("fileName", fileName);
        form.add("file", new FileSystemResource(Paths.get(FileSystemStorageTestUtil.class.getResource(resourceName).toURI()).toFile()));
        return new HttpEntity<>(form, headers);
    }

    @NotNull
    private static Map<String, String> readControlSums() throws IOException, URISyntaxException {
        return Files.readAllLines(Paths.get(FileSystemStorageTestUtil.class.getResource("/pl/sg/ip/control_sums.txt").toURI()))
                .stream()
                .map(line -> line.split(" ", -1))
                .collect(Collectors.toMap(
                        lineParts -> lineParts[0],
                        lineParts -> lineParts[1]
                ));
    }

    @NotNull
    private Task createTaskWithAttachment(int domainId, String fileName) throws IOException, URISyntaxException {
        var task = taskIntellectualProperty(domainId, List.of(fileName));
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage(FIRST_ATTACHMENT_IN_RESOURCE, fileName);
        task.setAttachments(List.of(fileName));
        taskRepository.save(task);
        commitAndStartNewTransaction();
        return task;
    }
}