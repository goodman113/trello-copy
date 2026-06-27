package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Attachment;
import my.project.trellocopy.entity.Task;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.projection.AttachmentForTaskProjection;
import my.project.trellocopy.entity.response.AttachmentDtoResponse;
import my.project.trellocopy.entity.response.AttachmentForTaskResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.AttachmentRepository;
import my.project.trellocopy.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    final AttachmentRepository repository;
    final S3Service s3Service;
    final TaskRepository taskRepository;

    public Object getAttachmentsForTask(Long taskId) {
        // Implementation for fetching attachments for a specific task
        List<AttachmentForTaskProjection> projection = repository.findAttachmentsByTaskId(taskId);
        return projection.stream().map(attachment -> new AttachmentForTaskResponse(
                attachment.getId(),
                attachment.getName(),
                attachment.getFileUrl(),
                attachment.getSize(),
                attachment.getContentType(),
                attachment.getCreatedAt()
        )).toList();
    }

    public Object createAttachment(Long taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        Attachment attachment = new Attachment();
        String s = s3Service.uploadFile(file);
        attachment.setFileUrl(s);
        attachment.setFileName(s.substring(s.lastIndexOf('/') + 1));
        attachment.setContentType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setTask(task);
        Attachment save = repository.save(attachment);

        return new AttachmentDtoResponse(
                save.getId(),
                save.getFileName(),
                save.getFileUrl(),
                save.getContentType(),
                save.getCreatedAt()
        );
    }

    public Object deleteAttachment(Long taskId, Long attachmentId) {
        Attachment attachment = repository.findById(attachmentId).orElseThrow(() -> RestException.restThrow(ErrorType.ATTACHMENT_NOT_FOUND));
        if (!attachment.getTask().getId().equals(taskId)) {
            throw RestException.restThrow(ErrorType.ATTACHMENT_NOT_FOUND);
        }
        s3Service.deleteFiles(List.of(attachment.getFileName()));
        repository.delete(attachment);
        return Map.of("message", "Attachment deleted successfully");
    }
}
