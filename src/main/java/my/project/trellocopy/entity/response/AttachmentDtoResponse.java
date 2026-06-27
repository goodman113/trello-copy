package my.project.trellocopy.entity.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttachmentDtoResponse(
        Long id,
        String fileName,
        String fileUrl,
        String contentType,
        LocalDateTime createAt
) {
}
