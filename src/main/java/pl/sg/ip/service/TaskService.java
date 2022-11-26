package pl.sg.ip.service;

import pl.sg.ip.api.TaskData;

import java.io.IOException;
import java.io.InputStream;

public interface TaskService {

    enum DeleteOutcome {
        DELETED, DONT_EXISTS
    }

    void update(int domainId, int intellectualPropertyId, TaskData updateData);

    void delete(int domainId, int taskId);

    void uploadAttachment(int domainId, int taskId, String fileName, InputStream inputStream) throws IOException;

    InputStream downloadAttachment(int domainId, int taskId, String fileName) throws IOException;

    DeleteOutcome deleteAttachment(int domainId, int taskId, String fileName);
}
