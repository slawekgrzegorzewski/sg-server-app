package pl.sg.ip.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface TaskAttachmentController {
    void uploadAttachment(int domainId, int taskId, String fileName, MultipartFile file) throws IOException;

    ResponseEntity<Resource> downloadAttachment(Integer domainId, int taskId, String fileName) throws IOException;

    ResponseEntity<Void> deleteAttachment(int domainId, int taskId, String fileName) throws IOException;
}
