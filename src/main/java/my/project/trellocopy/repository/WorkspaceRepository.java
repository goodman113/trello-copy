package my.project.trellocopy.repository;

import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceWorker;
import my.project.trellocopy.entity.projection.DashboardSearchProjection;
import my.project.trellocopy.entity.projection.WorkspaceProjection;
import my.project.trellocopy.entity.response.WorkspaceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findByOwnerId(Long ownerId);

    @Query("""
    select  w.id as id,
            w.name as name,
            w.description as description,
            count(distinct b.id) as boardCount,
            count(distinct ww.worker.id) as memberCount,
            w.color as color
     from Workspace w join Board b on b.workspace = w
     join WorkspaceWorker ww on ww.workspace = w
     where w.owner = :user or ww.worker = :user
""")
    List<WorkspaceProjection> getWorkspacesDatas(User user);

    @Query("""
        select w from Workspace w join WorkspaceWorker ww on ww.workspace = w
        where w.id = :id and (w.owner = :currentUser or ww.worker = :currentUser)
""")
    Optional<Workspace> findByIdAndOwnerOrIsWorker(Long id, User currentUser);
    @Query("""
        select distinct
         b.id as boardId,
         b.name as boardTitle,
         b.color as boardColor,
         t.id as taskId,
         t.title as taskTitle,
         t.boardColumn.board.id as taskBoardId,
         wk.worker.id as memberId,
         wk.worker.username as memberName,
         wk.worker.avatarUrl as avatarUrl
        from Workspace w
        join WorkspaceWorker wk on wk.workspace = w
        join Board b on b.workspace = w
        join BoardColumn bc on bc.board = b
        join Task t on t.boardColumn = bc
        where (:query is null or
               b.name like :query or
               t.title like :query or
               wk.worker.username like :query or
               wk.worker.fullName like :query)
""")
    List<DashboardSearchProjection> search(String query, Long workspaceId);

    @Query("""
        select distinct ww
        from WorkspaceWorker ww
        where ww.workspace = :workspace
""")
    List<WorkspaceWorker> getWorkspaceMembers(Workspace workspace);

    @Query("""
        select count(ww) from WorkspaceWorker ww where ww.workspace.id = :workspaceId
""")
    Long getWorkspaceMembersCount(Long workspaceId);
}
