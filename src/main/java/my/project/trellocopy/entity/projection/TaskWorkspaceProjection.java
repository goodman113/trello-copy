package my.project.trellocopy.entity.projection;

public interface TaskWorkspaceProjection {
    Long getId();
    String getTitle();
    String getStatus();
    String getPriority();
    String getDueDate();
    String getUserId();
    String getUsername();
    String getAvatar();
    Long getBoardColumnId();
    String getBoardColumnName();
    Integer getCommentCount();
}
