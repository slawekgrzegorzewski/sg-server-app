package pl.sg.ip.service.attachments;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.sg.AbstractContainerBaseTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@ActiveProfiles("dev")
class FileSystemTaskAttachmentStorageServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private FileSystemTaskAttachmentStorageService fileSystemTaskAttachmentStorageService;

    @Value("${intellectual-property.task.attachments.storage.dir}")
    private Path storagePath;

    private final Path testResource = Paths.get("/", "pl", "sg", "ip", "service", "attachments", "file.txt");
    private final String fileName = "test.txt";
    private final int intellectualPropertyId = 1;
    private final int taskId = 2;
    private Path subdirectory;
    private Path filePath;

    @BeforeEach
    public void beforeEach() throws IOException {
        subdirectory = storagePath.resolve(String.valueOf(intellectualPropertyId)).resolve(String.valueOf(taskId));
        filePath = subdirectory.resolve(fileName);
        delete(storagePath);
        assertFalse(Files.exists(storagePath));
    }

    @AfterEach
    public void afterEach() throws IOException {
        delete(storagePath);
        assertFalse(Files.exists(storagePath));
    }

    private void delete(Path dir) throws IOException {
        if (Files.exists(dir)) {
            System.out.println(dir.toString());
            if (Files.isDirectory(dir)) {
                try (Stream<Path> list = Files.list(dir)) {
                    list.forEach(file -> {
                        try {
                            delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
            Files.delete(dir);
        }
    }

    @Test
    void testListOnNonInitializedDir() {
        Collection<String> files = fileSystemTaskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertTrue(files.isEmpty());
    }

    @Test
    void testListOnEmptyDir() throws IOException {
        Files.createDirectories(subdirectory);
        Collection<String> files = fileSystemTaskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertTrue(files.isEmpty());
    }

    @Test
    void testList() throws URISyntaxException, IOException {
        Files.createDirectories(subdirectory);
        Files.copy(
                Paths.get(FileSystemTaskAttachmentStorageServiceTest.class.getResource(testResource.toString()).toURI()),
                filePath.toAbsolutePath());
        Collection<String> files = fileSystemTaskAttachmentStorageService.listFiles(intellectualPropertyId, taskId);
        assertEquals(1, files.size());
        assertEquals(fileName, files.iterator().next());
    }

    @Test
    void testPut() throws IOException {
        boolean putSuccessful = fileSystemTaskAttachmentStorageService.putFile(
                intellectualPropertyId,
                taskId,
                fileName,
                Objects.requireNonNull(getClass().getResourceAsStream(testResource.toString())));
        assertTrue(putSuccessful);
        assertTrue(Files.exists(subdirectory));
        assertEquals("Test content", Files.readString(filePath));
    }

}