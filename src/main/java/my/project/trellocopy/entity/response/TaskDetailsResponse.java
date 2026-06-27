package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailsResponse(
        Long id,
        String title,
        String description,
        String priority,
        String taskStatus,
        String dueDate,

        UserDtoResponse assignee,

        BoardColumnTaskDetailsResponse boardColumn,

        BoardTaskDetailsResponse board,

        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> tags,
        Long workspaceId
) {
}
