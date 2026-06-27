package my.project.trellocopy.repository;

import my.project.trellocopy.entity.ActivityLog;
import my.project.trellocopy.entity.projection.ActivityLogByWorkspaceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    @Query("""
        select a.id as id,
         a.type as type,
          a.message as message,
           a.actor.id as userId,
            a.actor.username as username,
             a.actor.avatarUrl as avatar,
             a.createdAt as timestamp
        from ActivityLog a
        where a.board.workspace.id = :workspaceId
""")
    List<ActivityLogByWorkspaceProjection> getActivityLogByWorkspace(Long workspaceId);

    @Query("""
        select a.id as id,
         a.type as type,
          a.message as message,
           a.actor.id as userId,
            a.actor.username as username,
             a.actor.avatarUrl as avatar,
             a.createdAt as timestamp
        from ActivityLog a
        where a.task.id = :taskId
""")
    List<ActivityLogByWorkspaceProjection> getActivityLogByTask(Long taskId);
}
