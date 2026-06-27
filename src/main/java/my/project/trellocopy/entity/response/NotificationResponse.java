package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String type,
        String title,
        String message,
        Boolean read,
        LocalDateTime createdAt,
        UserDtoResponse actor,
        Long taskId,
        Long boardId,
        Long workspaceId
) {}