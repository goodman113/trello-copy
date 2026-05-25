package my.project.trellocopy.entity.response;

public record WorkspaceResponse(
        Long id,
        String name,
        String description,
        Long boardCount,
        Long memberCount,
        String color
) {
}
