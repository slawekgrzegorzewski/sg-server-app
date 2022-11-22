package pl.sg.ip.service;

import org.springframework.web.multipart.MultipartFile;
import pl.sg.ip.api.TaskData;

import java.io.IOException;

public interface TaskService {
    void update(int domainId, int intellectualPropertyId, TaskData updateData);

    void uploadAttachment(int domainId, int taskId, String fileName, MultipartFile file) throws IOException;
}
