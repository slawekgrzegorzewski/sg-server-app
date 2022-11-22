package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.sg.application.ForbiddenException;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.Task;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.service.attachments.TaskAttachmentStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Component
public class TaskJPAService implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskAttachmentStorageService taskAttachmentStorageService;

    public TaskJPAService(TaskRepository taskRepository, TaskAttachmentStorageService taskAttachmentStorageService) {
        this.taskRepository = taskRepository;
        this.taskAttachmentStorageService = taskAttachmentStorageService;
    }

    @Override
    public void update(int domainId, int taskId, TaskData updateData) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!new IPValidator(task.getIntellectualProperty()).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to update tasks from IP from other domain.");
        }
        task.setDescription(updateData.description());
        task.setCoAuthors(updateData.coAuthors());
        taskRepository.save(task);
    }

    @Override
    public void uploadAttachment(int domainId, int taskId, String fileName, MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!new IPValidator(task.getIntellectualProperty()).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to upload attachment for task from other domain.");
        }
        if (task.getAttachments().contains(fileName)) {
            throw new IPException("Trying to reupload existing attachment");
        }
        try (InputStream inputStream = file.getInputStream()) {
            taskAttachmentStorageService.putFile(task.getIntellectualProperty().getId(), taskId, fileName, inputStream);
            ArrayList<String> attachments = new ArrayList<>(task.getAttachments());
            attachments.add(fileName);
            task.setAttachments(attachments);
            taskRepository.save(task);
        }
    }
}
