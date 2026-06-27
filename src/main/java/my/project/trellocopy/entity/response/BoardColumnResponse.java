package my.project.trellocopy.entity.response;

public record BoardColumnResponse(
        Long id,
        String title,
        Long position,
        Long boardId
) {
}
