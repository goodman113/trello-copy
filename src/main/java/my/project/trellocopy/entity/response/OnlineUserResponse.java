package my.project.trellocopy.entity.response;

public record OnlineUserResponse(
        Long id,
        String name,
        String avatarUrl,
        String status
) {
}
