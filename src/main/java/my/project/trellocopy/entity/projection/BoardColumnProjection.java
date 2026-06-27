package my.project.trellocopy.entity.projection;

import java.time.LocalDateTime;

public interface BoardColumnProjection {
    Long getId();
    String getColumnTitle();
    Long getPosition();
    Long getTaskId();
    String getTaskTitle();
    String getTaskDescription();
    String getPriority();
    String getStatus();
    Long getUserId();
    String getUsername();
    String getUserAvatar();
    LocalDateTime getDueDate();
    Long getBoardColumnId();
    Long getTaskPosition();
    Integer getCommentCount();
}
