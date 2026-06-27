package my.project.trellocopy.entity.response;

import my.project.trellocopy.entity.enums.InvitationStatus;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;

import java.time.LocalDateTime;

public record InvitationDetailResponse(
        Long workspaceId,
        String workspaceName,
        String workspaceColor,
        UserDtoResponse inviter,
        String inviteeEmail,
        WorkspaceWorkerRole role,
        String token,
        InvitationStatus status,
        LocalDateTime expiresAt,
        Long workspaceMemberCount
) {
}
