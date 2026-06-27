package my.project.trellocopy.entity.projection;

public interface UserSearchProjection {
    Long getId();
    String getEmail();
    String getUsername();
    String getFullName();
    String getAvatarUrl();
    boolean getAlreadyMember();
    boolean getAlreadyInvited();
}
