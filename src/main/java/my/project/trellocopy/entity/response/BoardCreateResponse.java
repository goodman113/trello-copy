package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record BoardCreateResponse(Long id,
                                  String title,
                                  String color,
                                  Integer taskCount,
                                  LocalDateTime createdAt) {
}
