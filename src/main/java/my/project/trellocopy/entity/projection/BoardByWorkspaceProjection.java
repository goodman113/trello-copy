package my.project.trellocopy.entity.projection;

import java.time.LocalDateTime;

public interface BoardByWorkspaceProjection {
    Long getId();
    String getTitle();
    String getDescription();
    String getColor();
    Long  getTaskCount();
    LocalDateTime getCreatedAt();

}
