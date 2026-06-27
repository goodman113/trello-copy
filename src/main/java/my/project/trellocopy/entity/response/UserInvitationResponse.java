package my.project.trellocopy.entity.response;

import my.project.trellocopy.entity.enums.InvitationStatus;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;

import java.time.LocalDateTime;

public record UserInvitationResponse(
        Long id,
        Long workspaceId,
        Long inviterId,
        String inviterName,
        String inviterAvatarUrl,
        String inviteeEmail,
        WorkspaceWorkerRole role,
        InvitationStatus status,
        LocalDateTime createAt
) {
}
