package pl.sg.ip.service.attachments;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

public interface TaskAttachmentStorageService {

    Collection<String> listFiles(int intellectualPropertyId, int taskId);

    Optional<InputStream> getFile(int intellectualPropertyId, int taskId, String fileName);

    boolean putFile(int intellectualPropertyId, int taskId, String fileName, InputStream fileStream);

    boolean deleteFile(int intellectualPropertyId, int taskId, String fileName);
}
