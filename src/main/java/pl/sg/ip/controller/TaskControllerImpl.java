package pl.sg.ip.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.service.TaskService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/task")
public class TaskControllerImpl implements TaskController {

    private final ModelMapper modelMapper;
    private final TaskService taskService;

    public TaskControllerImpl(ModelMapper modelMapper, TaskService taskService) {
        this.modelMapper = modelMapper;
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

    @Override
    @PostMapping(path = "{id}/attachments")
    @TokenBearerAuth(any = "IPR")
    public void uploadAttachment(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int taskId,
            @RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file) throws IOException {
        taskService.uploadAttachment(domainId, taskId, fileName, file);
    }
}
