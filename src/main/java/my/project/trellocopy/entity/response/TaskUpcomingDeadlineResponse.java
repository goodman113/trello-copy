package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record TaskUpcomingDeadlineResponse(
        Long id,
        String taskTitle,
        LocalDateTime dueDate,
        String boardName,
        UserDtoResponse assignee
) {
}
