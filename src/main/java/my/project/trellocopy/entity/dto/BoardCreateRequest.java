package my.project.trellocopy.entity.dto;

public record BoardCreateRequest(
        Long workspaceId,
        String title,
        String description,
        String color
) {

}
