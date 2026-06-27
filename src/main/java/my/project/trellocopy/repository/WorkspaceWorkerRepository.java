package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceWorkerRepository extends JpaRepository<WorkspaceWorker, Long> {

    @Query("""
        select ww
        from WorkspaceWorker ww
        where ww.workspace.id = :workspaceId
    """)
    List<WorkspaceWorker> findAllWorkersByWorkspace(Long workspaceId);

    Optional<WorkspaceWorker> findWorkspaceWorkerByIdAndWorkspace(Long id, Workspace workspace);
}
