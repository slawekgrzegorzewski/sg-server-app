package pl.sg.ip.controller;

import org.springframework.web.bind.annotation.*;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    public TaskControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PatchMapping(value = "/{id}", produces = {"plain/text"})
    @TokenBearerAuth(any = "IPR")
    public void update(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int taskId,
            @RequestBody TaskData updateData) {
        taskService.update(domainId, taskId, updateData);
    }
}
