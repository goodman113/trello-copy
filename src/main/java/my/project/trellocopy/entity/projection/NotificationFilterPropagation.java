package my.project.trellocopy.entity.projection;

import java.time.LocalDateTime;

public interface NotificationFilterPropagation {
    Long getId();
    String getType();
    String getTitle();
    String getMessage();
    Boolean getRead();
    LocalDateTime getCreatedAt();
    Long getActorId();
    String getActorName();
    String getAvatarUrl();
    long getTaskId();
    long getBoardId();
    Long getWorkspaceId();

}
