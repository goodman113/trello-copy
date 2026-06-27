package my.project.trellocopy.entity.response;

import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;

public record WorkSpaceMembersResponse(Long id,
                                       Long userId,
                                       String username,
                                       String avatarUrl,
                                       WorkspaceWorkerRole role,
                                       String email,
                                       Boolean isOwner,
                                       String fullName
                                       ) {
}
