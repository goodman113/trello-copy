package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record AttachmentForTaskResponse(
        Long id,
        String fileName,
        String fileUrl,
        Double fileSize,
        String contentType,
        LocalDateTime createAt
) {
}
