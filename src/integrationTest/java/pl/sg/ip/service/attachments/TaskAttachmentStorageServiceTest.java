package pl.sg.ip.service.attachments;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sg.AbstractContainerBaseTest;
import pl.sg.ip.service.attachments.utils.StorageTestUtil;
import pl.sg.ip.service.attachments.utils.StorageTestUtilFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@ActiveProfiles(value = {"dev", "fileSystemStorage"})
//@ActiveProfiles(value = {"dev", "S3Storage"})
class TaskAttachmentStorageServiceTest extends AbstractContainerBaseTest {

    public static final String FIRST_FILE_CONTENT = "Test content";
    public static final String SECOND_FILE_CONTENT = "Test content2";

    @Autowired
    private TaskAttachmentStorageService taskAttachmentStorageService;
    @Autowired
    private StorageTestUtilFactory storageTestUtilFactory;

    private final String testResource = "/pl/sg/ip/service/attachments/file.txt";
    private final String secondTestResource = "/pl/sg/ip/service/attachments/file2.txt";
    private final String fileName = "test.txt";
    private final String secondFileName = "test2.txt";
    private final int intellectualPropertyId = 1;
    private final int taskId = 2;
    private StorageTestUtil storageTestUtil;

    @BeforeEach
    public void beforeEach() throws IOException {
        storageTestUtil = storageTestUtilFactory.create(intellectualPropertyId, taskId);
        storageTestUtil.clearStorage();
    }

    @AfterEach
    public void afterEach() throws IOException {
        storageTestUtil.clearStorage();
    }

    @Test
    void testListOnNonInitializedDir() {
        Collection<String> files = taskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertTrue(files.isEmpty());
    }

    @Test
    void testListOnEmptyDir() throws IOException {
        storageTestUtil.initStorage();
        Collection<String> files = taskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertTrue(files.isEmpty());
    }

    @Test
    void testList() throws URISyntaxException, IOException {
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage(testResource, fileName);
        Collection<String> files = taskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertEquals(1, files.size());
        assertEquals(fileName, files.iterator().next());
    }

    @Test
    void testPut() throws IOException {
        storageTestUtil.initStorage();
        boolean putSuccessful = taskAttachmentStorageService.putFile(
                intellectualPropertyId,
                taskId,
                fileName,
                Objects.requireNonNull(getClass().getResourceAsStream(testResource)));
        assertTrue(putSuccessful);
        assertEquals("Test content", storageTestUtil.readFileContentFromStorage(fileName));
    }

    @Test
    void testPutOverwritePreviousFile() throws IOException {
        storageTestUtil.initStorage();

        boolean putSuccessful = taskAttachmentStorageService.putFile(intellectualPropertyId, taskId, fileName, Objects.requireNonNull(getClass().getResourceAsStream(testResource)));
        assertTrue(putSuccessful);
        assertEquals("Test content", storageTestUtil.readFileContentFromStorage(fileName));

        putSuccessful = taskAttachmentStorageService.putFile(intellectualPropertyId, taskId, fileName, Objects.requireNonNull(getClass().getResourceAsStream(secondTestResource)));
        assertTrue(putSuccessful);
        assertEquals(SECOND_FILE_CONTENT, storageTestUtil.readFileContentFromStorage(fileName));
    }

    @Test
    void testGetOnNotInitializedDir() {
        Optional<InputStream> file = taskAttachmentStorageService.getFile(intellectualPropertyId, taskId, fileName);
        assertTrue(file.isEmpty());
    }

    @Test
    void testGetOnEmptyDir() throws IOException {
        storageTestUtil.initStorage();
        Optional<InputStream> file = taskAttachmentStorageService.getFile(intellectualPropertyId, taskId, fileName);
        assertTrue(file.isEmpty());
    }

    @Test
    void testGet() throws IOException, URISyntaxException {
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage(testResource, fileName);
        try (InputStream inputStream = taskAttachmentStorageService.getFile(intellectualPropertyId, taskId, fileName).orElseThrow()) {
            String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            assertEquals(FIRST_FILE_CONTENT, text);
        }
    }

    @Test
    void testDeleteOnNotInitializedDir() {
        boolean deleted = taskAttachmentStorageService.deleteFile(intellectualPropertyId, taskId, fileName);
        assertFalse(deleted);
    }

    @Test
    void testDeleteOnEmptyDir() throws IOException {
        storageTestUtil.initStorage();
        taskAttachmentStorageService.deleteFile(intellectualPropertyId, taskId, fileName);
    }

    @Test
    void testDelete() throws IOException, URISyntaxException {
        storageTestUtil.initStorage();
        storageTestUtil.putResourceInStorage(testResource, fileName);
        storageTestUtil.putResourceInStorage(secondTestResource, secondFileName);
        boolean deleted = taskAttachmentStorageService.deleteFile(intellectualPropertyId, taskId, fileName);
        assertTrue(deleted);
        assertFalse(storageTestUtil.checkFileExistenceInStorage(fileName));
        assertTrue(storageTestUtil.checkFileExistenceInStorage(secondFileName));
    }

}