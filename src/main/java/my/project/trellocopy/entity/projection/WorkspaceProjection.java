package my.project.trellocopy.entity.projection;

public interface WorkspaceProjection {
    Long getId();
    String getName();
    String getDescription();
    Long getBoardCount();
    Long getMemberCount();
    String getColor();
}
