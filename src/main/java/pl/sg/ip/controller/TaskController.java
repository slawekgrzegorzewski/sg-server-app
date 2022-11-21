package pl.sg.ip.controller;

import org.springframework.web.multipart.MultipartFile;


public interface TaskController {
    void uploadAttachment(int domainId, int taskId, MultipartFile file, String fileName);
}
