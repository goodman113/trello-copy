package my.project.trellocopy.repository;

import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.response.WorkspaceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
    List<WorkspaceResponse> getWorkspacesDatas(User user);

}
