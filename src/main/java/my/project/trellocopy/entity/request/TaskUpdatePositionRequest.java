package my.project.trellocopy.entity.request;

public record TaskUpdatePositionRequest(
        Long taskId,
        Long position
) {
}
