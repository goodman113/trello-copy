package my.project.trellocopy.entity.request;

import java.time.LocalDateTime;

public record TaskUpdateRequest(

        String title,
        String description,
        String priority,
        Long assigneeId,
        LocalDateTime dueDate,
        Long boardColumnId
) {
}
