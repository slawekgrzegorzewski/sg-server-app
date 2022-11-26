package pl.sg.ip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.Task;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.service.attachments.TaskAttachmentStorageService;
import pl.sg.ip.service.validator.Validator;
import pl.sg.ip.service.validator.ValidatorFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class TaskJPAService implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskJPAService.class);
    private final TaskRepository taskRepository;
    private final TaskAttachmentStorageService taskAttachmentStorageService;
    private final ValidatorFactory validatorFactory;

    public TaskJPAService(TaskRepository taskRepository, TaskAttachmentStorageService taskAttachmentStorageService, ValidatorFactory validatorFactory) {
        this.taskRepository = taskRepository;
        this.taskAttachmentStorageService = taskAttachmentStorageService;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public void update(int domainId, int taskId, TaskData updateData) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to update tasks from IP from other domain.");
        }
        task.setDescription(updateData.description());
        task.setCoAuthors(updateData.coAuthors());
        taskRepository.save(task);
    }

    @Override
    public void delete(int domainId, int taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        Validator validator = validatorFactory.validator(task);
        if (!validator.validateDomain(domainId)) {
            throw new ForbiddenException("Trying to delete tasks from IP from other domain.");
        }
        if (!validator.validateDeletion()) {
            throw new IPException("This entity refers time record and thus can not be deleted");
        }
        taskRepository.delete(task);
    }

    @Override
    public void uploadAttachment(int domainId, int taskId, String fileName, InputStream inputStream) throws IOException {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to upload attachment for task from other domain.");
        }
        if (task.getAttachments().contains(fileName)) {
            throw new IPException("Trying to reupload existing attachment");
        }
        try (inputStream) {
            taskAttachmentStorageService.putFile(task.getIntellectualProperty().getId(), taskId, fileName, inputStream);
            ArrayList<String> attachments = new ArrayList<>(task.getAttachments());
            attachments.add(fileName);
            task.setAttachments(attachments);
            taskRepository.save(task);
        }
    }

    @Override
    public InputStream downloadAttachment(int domainId, int taskId, String fileName) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to upload attachment for task from other domain.");
        }
        Optional<InputStream> file = taskAttachmentStorageService.getFile(task.getIntellectualProperty().getId(), taskId, fileName);
        if (!task.getAttachments().contains(fileName)) {
            if (file.isPresent()) {
                LOG.warn("A file - {} - exists in storage but is not present in task's attachments, clearing storage.", fileName);
                try {
                    file.get().close();
                } catch (IOException e) {
                    LOG.warn("Problem during stream closing");
                } finally {
                    taskAttachmentStorageService.deleteFile(task.getIntellectualProperty().getId(), taskId, fileName);
                }
            }
            throw new NoSuchElementException("Attachment not connected to Task");
        }
        if (file.isEmpty()) {
            LOG.warn("A file - {} - from task's attachment is not present in storage, clearing DB", fileName);
            ArrayList<String> attachments = new ArrayList<>(task.getAttachments());
            attachments.remove(fileName);
            task.setAttachments(attachments);
            taskRepository.save(task);
            throw new NoSuchElementException("File not present at storage");
        }
        return file.get();
    }

    @Override
    public DeleteOutcome deleteAttachment(int domainId, int taskId, String fileName) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!validatorFactory.validator(task).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to delete attachment for task from other domain.");
        }
        List<String> attachments = new ArrayList<>(task.getAttachments());
        if (!attachments.contains(fileName)) {
            throw new NoSuchElementException("Attachment not connected to Task");
        }
        attachments.remove(fileName);
        task.setAttachments(attachments);
        taskRepository.save(task);
        boolean fileExists = taskAttachmentStorageService.listFiles(task.getIntellectualProperty().getId(), taskId)
                .contains(fileName);
        if (fileExists) {
            taskAttachmentStorageService.deleteFile(task.getIntellectualProperty().getId(), taskId, fileName);
            return DeleteOutcome.DELETED;
        }
        return DeleteOutcome.DONT_EXISTS;
    }
}
