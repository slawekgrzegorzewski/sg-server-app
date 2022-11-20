package pl.sg.ip.service.attachments;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.sg.application.configuration.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@Profile("dev")
public class FileSystemTaskAttachmentStorageService implements TaskAttachmentStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(FileSystemTaskAttachmentStorageService.class);

    @Value("${intellectual-property.task.attachments.storage.dir}")
    private String storagePath;

    @Override
    public Collection<String> listFiles(int intellectualPropertyId, int taskId) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try (Stream<Path> listOfFiles = Files.list(getStorage())) {
            return listOfFiles
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.warn("Unable to list files under directory " + prefix + " due to an exception.", e);
            return List.of();
        }
    }

    @Override
    public Optional<InputStream> getFile(int intellectualPropertyId, int taskId, String fileName) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            return of(new FileInputStream(getStorage().resolve(fileName).toFile()));
        } catch (IOException e) {
            LOG.warn("Unable to get file " + fileName + " from directory " + prefix + " due to an exception.", e);
            return empty();
        }
    }

    @Override
    public boolean putFile(int intellectualPropertyId, int taskId, String fileName, InputStream fileStream) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            createClient().putObject(bucketName, prefix + fileName, fileStream, new ObjectMetadata());
            return true;
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to put file " + fileName + " under directory " + prefix + " due to an exception.", e);
            return false;
        }
    }

    @Override
    public boolean deleteFile(int intellectualPropertyId, int taskId, String fileName) {
        String prefix = buildPrefix(intellectualPropertyId, taskId);
        try {
            createClient().deleteObject(bucketName, prefix + fileName);
            return true;
        } catch (AmazonServiceException e) {
            LOG.warn("Unable to delete file " + fileName + " from directory " + prefix + " due to an exception.", e);
            return false;
        }
    }

    private Path getStorage() throws IOException {
        var path = Paths.get(storagePath);
        Files.createDirectories(path);
        return path;
    }

    private String buildPrefix(int intellectualPropertyId, int taskId) {
        return String.format("%s/%s/", intellectualPropertyId, taskId);
    }
}
