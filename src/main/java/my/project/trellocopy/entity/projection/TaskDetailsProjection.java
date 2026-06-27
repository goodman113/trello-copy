package my.project.trellocopy.entity.projection;

import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.response.UserDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskDetailsProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getPriority();
    String getTaskStatus();
    String getDueDate();

    Long getUserId();
    String getUsername();
    String getAvatar();

    Long getColumnId();
    String getColumnTitle();

    Long getBoardId();
    String getBoardTitle();

    String getCreatedBy();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    List<String> getTags();
    Long getWorkspaceId();

}
