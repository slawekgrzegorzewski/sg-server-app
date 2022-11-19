package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.Task;
import pl.sg.ip.repository.TaskRepository;

@Component
public class TaskJPAService implements TaskService {

    private final TaskRepository taskRepository;

    public TaskJPAService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
}
