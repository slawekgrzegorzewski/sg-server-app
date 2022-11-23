package pl.sg.ip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedUploadAttachmentRequest() throws URISyntaxException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                headers(DEFAULT_DOMAIN_ID),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDownloadAttachmentRequest() throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void shouldFailUnauthenticatedDeleteAttachmentRequest() throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers(DEFAULT_DOMAIN_ID)),
                String.class);

        assertEquals(401, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void getTaskRolesAccess(String role) {
        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.GET,
                new HttpEntity<String>(authenticatedHeaders(DEFAULT_DOMAIN_ID, role)),
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void createTaskRolesAccess(String role) throws JsonProcessingException {

        var intellectualProperty = createBasicIntellectualPropertyForDomain(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToCreate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathFromIntellectualProperty(intellectualProperty.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void updateTaskRolesAccess(String role) throws JsonProcessingException {

        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TaskData testTaskToUpdate = new TaskData("", "");
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testTaskToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void uploadAttachmentRequestRolesAccess(String role) throws URISyntaxException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, role),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void downloadAttachmentRequestRolesAccess(String role) throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), fileName, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{role})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(403, response.getStatusCode().value());
    }


    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    void deleteAttachmentRequestRolesAccess(String role) throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(403, response.getStatusCode().value());
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
                pathForTask(task.getId() + 1),
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
                pathForTask(task.getId()),
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
                pathForTask(task.getId()),
                HttpMethod.PATCH,
                requestEntity,
                String.class);
        assertEquals(200, response.getStatusCode().value());
        List<Task> allTasks = taskRepository.findAll();
        assertEquals(1, allTasks.size());
        assertEquals(TASK_CO_AUTHORS, allTasks.get(0).getCoAuthors());
        assertEquals(TASK_DESCRIPTION, allTasks.get(0).getDescription());
    }

    @Test
    void shouldFailUploadAttachmentRequestForNonExistingTask() throws URISyntaxException {
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(1),
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldFailUploadAttachmentRequestForTaskFromOtherDomain() throws URISyntaxException {
        var task = createBasicTaskWithIntellectualProperty(SECOND_DOMAIN_ID);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                "first.pdf",
                "/pl/sg/ip/attachment1.pdf");

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldUploadAttachmentRequest() throws URISyntaxException, IOException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        String fileName = "first.pdf";
        String resourceName = "attachment1.pdf";
        String resourcePath = "/pl/sg/ip/" + resourceName;
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                fileName,
                resourcePath);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        task = taskRepository.getReferenceById(task.getId());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, task.getAttachments().size());
        assertEquals(fileName, task.getAttachments().get(0));

        try (InputStream inputStream = taskAttachmentStorageService.getFile(task.getIntellectualProperty().getId(), task.getId(), fileName)
                .orElseThrow(() -> new AssertionFailedError("expected a file here"))) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(resourceName), hash.toString());
        }
    }

    @Test
    void shouldKeepOriginalFileWhenReuploadAttachmentRequest() throws URISyntaxException, IOException {
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        String fileName = "first.pdf";
        String resourceName = "attachment1.pdf";
        String resourcePath = "/pl/sg/ip/" + resourceName;
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                fileName,
                resourcePath);

        restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        String otherResourcePath = "/pl/sg/ip/attachment2.pdf";
        requestEntity = prepareUploadForm(
                authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR"),
                fileName,
                otherResourcePath);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachment(task.getId()),
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(400, response.getStatusCode().value());

        try (InputStream inputStream = taskAttachmentStorageService.getFile(task.getIntellectualProperty().getId(), task.getId(), fileName)
                .orElseThrow(() -> new AssertionFailedError("expected a file here"))) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(resourceName), hash.toString());
        }
    }

    @Test
    void shouldFailDownloadingAttachmentForTaskInOtherDomain() throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        var task = createBasicTaskWithIntellectualProperty(SECOND_DOMAIN_ID);
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage("/pl/sg/ip/attachment1.pdf", fileName);
        task.setAttachments(List.of(fileName));
        taskRepository.save(task);
        commitAndStartNewTransaction();

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), fileName, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailDownloadingForNonExistingAttachmentAndUpdateTaskIfThisAttachmentWasThere() {
        String fileName = "first.pdf";
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        task.setAttachments(List.of(fileName));
        taskRepository.save(task);
        commitAndStartNewTransaction();

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), fileName, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(404, response.getStatusCode().value());
        commitAndStartNewTransaction();
        task = taskRepository.getReferenceById(task.getId());
        assertTrue(task.getAttachments().isEmpty());
    }

    @Test
    void shouldFailDownloadingForAttachmentNotRelatedInTaskAndRemoveItFromStorage() throws IOException, URISyntaxException {
        String fileName = "first.pdf";
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage("/pl/sg/ip/attachment1.pdf", fileName);

        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), fileName, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(404, response.getStatusCode().value());
        assertFalse(storageTestUtil.checkFileExistenceInStorage(fileName));
    }

    @Test
    void shouldDownloadAttachment() throws IOException, URISyntaxException {
        String fileName = "first.pdf";
        var task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        String resourceName = "attachment1.pdf";
        storageTestUtil.putResourceInStorage("/pl/sg/ip/" + resourceName, fileName);
        task.setAttachments(List.of(fileName));
        taskRepository.save(task);
        commitAndStartNewTransaction();

        ResponseEntity<Resource> response = restTemplate.exchange(
                pathForAttachmentDownloadWithAuthentication(task.getId(), fileName, DEFAULT_DOMAIN_ID, generateJWTToken(new String[]{"IPR"})),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Resource.class);

        assertEquals(200, response.getStatusCode().value());

        try (InputStream inputStream = Objects.requireNonNull(response.getBody()).getInputStream()) {
            Hasher hasher = Hashing.sha512().newHasher();
            ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
            HashCode hash = hasher.hash();
            Map<String, String> controlSums = readControlSums();
            assertEquals(controlSums.get(resourceName), hash.toString());
        }
    }

    @Test
    void shouldFailDeleteAttachmentRequestBelongingToOtherDomain() throws URISyntaxException, IOException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        HttpHeaders headers = authenticatedHeaders(SECOND_DOMAIN_ID, "IPR");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void shouldFailDeleteAttachmentRequestForNonExistingAttachment() {
        String fileName = "first.pdf";
        Task task = createBasicTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void shouldDeleteAttachment() throws IOException, URISyntaxException {
        String fileName = "first.pdf";
        Task task = createTaskWithAttachment(fileName);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(200, response.getStatusCode().value());
        assertFalse(storageTestUtil.checkFileExistenceInStorage(fileName));
        commitAndStartNewTransaction();
        task = taskRepository.getReferenceById(task.getId());
        assertFalse(task.getAttachments().contains(fileName));
    }

    @Test
    void shouldDeleteReferencedButNotPresentInStorageAttachment() {
        String fileName = "first.pdf";
        Task task = createTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID, "", "", List.of(fileName));

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        ResponseEntity<?> response = restTemplate.exchange(
                pathForAttachmentDownload(task.getId(), fileName),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(299, response.getStatusCodeValue());
    }

    @NotNull
    private String pathFromIntellectualProperty(int intellectualPropertyId) {
        return "http://localhost:%d/ipr/%d/task".formatted(serverPort, intellectualPropertyId);
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
    private Task createTaskWithAttachment(String fileName) throws IOException, URISyntaxException {
        var task = createTaskWithIntellectualProperty(DEFAULT_DOMAIN_ID, "", "", List.of(fileName));
        storageTestUtil = storageTestUtilFactory.create(task.getIntellectualProperty().getId(), task.getId());
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage("/pl/sg/ip/attachment1.pdf", fileName);
        task.setAttachments(List.of(fileName));
        taskRepository.save(task);
        commitAndStartNewTransaction();
        return task;
    }
}