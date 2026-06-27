package my.project.trellocopy.entity.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskCreateRequest(
        String title,
        String description,
        String status,
        String priority,
        Long assigneeId,
        LocalDateTime dueDate,
        Long boardColumnId
) {
}
