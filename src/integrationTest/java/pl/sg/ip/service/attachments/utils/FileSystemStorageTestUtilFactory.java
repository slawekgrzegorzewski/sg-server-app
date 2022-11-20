package pl.sg.ip.service.attachments.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Profile("fileSystemStorage")
public class FileSystemStorageTestUtilFactory implements StorageTestUtilFactory {


    @Value("${intellectual-property.task.attachments.storage.dir}")
    private Path storagePath;

    public StorageTestUtil create(int intellectualPropertyId, int taskId) {
        return new FileSystemStorageTestUtil(storagePath, intellectualPropertyId, taskId);
    }
}
