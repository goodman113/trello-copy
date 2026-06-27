package my.project.trellocopy.entity.response;

public record UserSearchResponse(
        Long id,
        String email,
        String username,
        String fullName,
        String avatarUrl,
        boolean alreadyMember,
        boolean alreadyInvited
) {
}
