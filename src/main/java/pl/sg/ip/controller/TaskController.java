package pl.sg.ip.controller;

import org.springframework.web.multipart.MultipartFile;
import pl.sg.ip.api.TaskData;

import java.io.IOException;


public interface TaskController {

    void update(int domainId, int taskId, TaskData taskData);
    void uploadAttachment(int domainId, int taskId, String fileName, MultipartFile file) throws IOException;
}
