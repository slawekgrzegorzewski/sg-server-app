package pl.sg.ip.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.service.attachments.TaskAttachmentStorageService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/iprTask")
public class TaskControllerImpl implements TaskController {

    private final ModelMapper modelMapper;
    private final TaskAttachmentStorageService taskAttachmentStorageService;

    public TaskControllerImpl(ModelMapper modelMapper, TaskAttachmentStorageService taskAttachmentStorageService) {
        this.modelMapper = modelMapper;
        this.taskAttachmentStorageService = taskAttachmentStorageService;
    }

    @Override
    @PostMapping(path = "{id}")
    @TokenBearerAuth(any = "IPR")
    public void uploadAttachment(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int taskId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName) {
        try (InputStream inputStream = file.getInputStream()) {
            taskAttachmentStorageService.putFile(1, taskId, fileName, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
