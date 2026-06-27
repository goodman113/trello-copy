package my.project.trellocopy.entity.projection;

import my.project.trellocopy.entity.response.OnlineUserResponse;

import java.time.LocalDateTime;

public interface UpcomingDeadLinesProjection {
    Long getId();
    String getTitle();
    LocalDateTime getDueDate();
    String getBoardName();
    Long getUserId();
    String getUsername();
    String getAvatar();
}
