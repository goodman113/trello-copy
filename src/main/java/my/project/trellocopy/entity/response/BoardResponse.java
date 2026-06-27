package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String title,
        String description,
        String color,
        Long taskCount,
        LocalDateTime createAt
) {
}
