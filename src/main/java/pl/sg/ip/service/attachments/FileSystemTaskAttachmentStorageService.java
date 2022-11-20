package pl.sg.ip.service.attachments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@Profile("fileSystemStorage")
public class FileSystemTaskAttachmentStorageService implements TaskAttachmentStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemTaskAttachmentStorageService.class);

    @Value("${intellectual-property.task.attachments.storage.dir}")
    private Path storagePath;

    @Override
    public Collection<String> listFiles(int intellectualPropertyId, int taskId) {
        try (Stream<Path> listOfFiles = Files.list(subfolder(intellectualPropertyId, taskId))) {
            return listOfFiles.map(Path::getFileName).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.warn("Unable to list files under directory " + intellectualPropertyId + "/" + taskId + "/ due to an exception.", e);
            return List.of();
        }
    }

    @Override
    public Optional<InputStream> getFile(int intellectualPropertyId, int taskId, String fileName) {
        try {
            return of(new FileInputStream(subfolder(intellectualPropertyId, taskId).resolve(fileName).toFile()));
        } catch (IOException e) {
            LOG.warn("Unable to get file " + fileName + " from directory  " + intellectualPropertyId + "/" + taskId + "/ due to an exception.", e);
            return empty();
        }
    }

    @Override
    public boolean putFile(int intellectualPropertyId, int taskId, String fileName, InputStream inputStream) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(subfolder(intellectualPropertyId, taskId).resolve(fileName).toFile())) {
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            return true;
        } catch (IOException e) {
            LOG.warn("Unable to put file " + fileName + " under directory " + intellectualPropertyId + "/" + taskId + "/ due to an exception.", e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(int intellectualPropertyId, int taskId, String fileName) {
        try {
            Files.delete(subfolder(intellectualPropertyId, taskId).resolve(fileName));
            return true;
        } catch (IOException e) {
            LOG.warn("Unable to delete file " + fileName + " from directory " + intellectualPropertyId + "/" + taskId + "/ due to an exception.", e);
            return false;
        }
    }

    private Path subfolder(int intellectualPropertyId, int taskId) throws IOException {
        Path subfolder = storagePath.resolve(String.valueOf(intellectualPropertyId)).resolve(String.valueOf(taskId));
        Files.createDirectories(subfolder);
        return subfolder;
    }
}
