package my.project.trellocopy.entity.projection;

import my.project.trellocopy.entity.enums.ActivityType;

import java.time.LocalDateTime;

public interface ActivityLogByWorkspaceProjection {
    Long getId();
    ActivityType getType();
    String getMessage();
    Long getUserId();
    String getUsername();
    String getAvatar();
    LocalDateTime getTimestamp();

}
