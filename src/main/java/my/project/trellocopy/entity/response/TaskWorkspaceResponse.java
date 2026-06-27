package my.project.trellocopy.entity.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskWorkspaceResponse(
        Long id,
        String title,
        String status,
        String priority,
        UserDtoResponse assignee,
        LocalDateTime dueDate,
        Long boardColumnId,
        String boardColumnName,
        Integer commentCount

) {
}
