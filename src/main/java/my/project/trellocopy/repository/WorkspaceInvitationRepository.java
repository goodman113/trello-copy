package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceInvitation;
import my.project.trellocopy.entity.enums.InvitationStatus;
import my.project.trellocopy.entity.projection.UserSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceInvitationRepository extends JpaRepository<WorkspaceInvitation, Long> {

    @Query("""
        select u.id,
                u.email,
                u.username,
                u.fullName,
                u.avatarUrl,
                case when ww.id is not null then true else false end as alreadyMember,
                case when wi.id is not null then true else false end as alreadyInvited
                
        from User u
        left join WorkspaceWorker ww on ww.workspace.id = :workspaceId and ww.worker.id = u.id
        left join WorkspaceInvitation wi on wi.workspace.id = :workspaceId
                                            and wi.inviteeEmail = u.email
                                            and wi.status = 'PENDING'
        where :ownerId != u.id and (
             :query is null
             or  lower(u.username) like lower(:query)
             or  lower(u.email) like lower(:query)
        )
        

""")
    List<UserSearchProjection> getWorkersInvitedAndMember(Long workspaceId, String query, Long ownerId);

    Optional<WorkspaceInvitation> getWorkspaceInvitationByToken(String token);

    @Query("""
        select w from WorkspaceInvitation w where w.workspace = :workspace and w.status = :status and w.expireAt >= :now and w.deleted = false

""")
    List<WorkspaceInvitation> findWorkspaceInvitationsByWorkspaceAndStatus(Workspace workspace, InvitationStatus status, LocalDateTime now);
}
