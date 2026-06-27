package my.project.trellocopy.entity.projection;

public interface DashboardSearchProjection {
    Long getBoardId();
    String getBoardTitle();
    String getBoardColor();
    Long getTaskId();
    String getTaskTitle();
    Long getTaskBoardId();
    Long getMemberId();
    String getMemberName();
    String getAvatarUrl();
}
