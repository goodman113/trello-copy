package my.project.trellocopy.entity.payload;

public record PresencePayload(
        String action,
        Long userId,
        String name,
        String avatarUrl
) {
}
