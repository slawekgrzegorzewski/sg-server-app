package pl.sg.ip.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.service.TaskService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/task")
public class TaskAttachmentControllerImpl implements TaskAttachmentController {

    private final TaskService taskService;

    public TaskAttachmentControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping(path = "{id}/attachment")
    @TokenBearerAuth(any = "IPR")
    public void uploadAttachment(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int taskId,
            @RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file) throws IOException {
        taskService.uploadAttachment(domainId, taskId, fileName, file.getInputStream());
    }

    @Override
    @GetMapping(path = "{id}/attachment/{fileName}")
    @TokenBearerAuth(any = "IPR", inQuery = true)
    @ResponseBody
    public ResponseEntity<Resource> downloadAttachment(
            @RequestParam(value = "domainId", required = false) Integer domainId,
            @PathVariable("id") int taskId,
            @PathVariable("fileName") String fileName
    ) throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"")
                .body(new InputStreamResource(taskService.downloadAttachment(domainId, taskId, fileName)));
    }

    @Override
    @DeleteMapping(path = "{id}/attachment/{fileName}")
    @TokenBearerAuth(any = "IPR")
    @ResponseBody
    public ResponseEntity<Void> deleteAttachment(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int taskId,
            @PathVariable("fileName") String fileName) {
        TaskService.DeleteOutcome outcome = taskService.deleteAttachment(domainId, taskId, fileName);
        if (outcome == TaskService.DeleteOutcome.DELETED) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(299).build();
        }
    }
}
