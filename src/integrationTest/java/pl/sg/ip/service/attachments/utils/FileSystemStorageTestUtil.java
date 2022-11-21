package pl.sg.ip.service.attachments.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileSystemStorageTestUtil implements StorageTestUtil {

    private final Path storagePath;
    private final Path subdirectory;

    public FileSystemStorageTestUtil(Path storagePath, int intellectualPropertyId, int taskId) {
        this.storagePath = storagePath;
        this.subdirectory = storagePath.resolve(String.valueOf(intellectualPropertyId)).resolve(String.valueOf(taskId));
    }

    @Override
    public boolean checkFileExistenceInStorage(String fileName) {
        return Files.exists(subdirectory.resolve(fileName));
    }

    @Override
    public void clearStorage() throws IOException {
        delete(storagePath);
    }

    private void delete(Path dir) throws IOException {
        if (Files.exists(dir)) {
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

    @Override
    public void initStorage() throws IOException {
        Files.createDirectories(subdirectory);
    }

    @Override
    public String readFileContentFromStorage(String fileName) throws IOException {
        return Files.readString(subdirectory.resolve(fileName));
    }

    @Override
    public void putResourceInStorage(String resource, String withName) throws IOException, URISyntaxException {
        Files.copy(Paths.get(FileSystemStorageTestUtil.class.getResource(resource).toURI()), subdirectory.resolve(withName));
    }
}
