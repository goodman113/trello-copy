package my.project.trellocopy.entity.response;

import my.project.trellocopy.entity.enums.ActivityType;

import java.time.LocalDateTime;

public record ActivityLogByWorkspaceResponse(
        Long id,
        ActivityType type,
        String message,
        UserDtoResponse user,
        LocalDateTime timestamp

) {
}
