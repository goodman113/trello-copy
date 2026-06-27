package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.WorkspaceWorker;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;
import my.project.trellocopy.repository.WorkspaceWorkerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceWorkerService {
    final WorkspaceWorkerRepository repository;

    public Object createWorkspaceWorker(Workspace workspace, User currentUser, WorkspaceWorkerRole role) {
        WorkspaceWorker worker = new WorkspaceWorker();
        worker.setWorkerRole(role);
        worker.setWorkspace(workspace);
        worker.setWorker(currentUser);

        WorkspaceWorker save = repository.save(worker);
        return save;
    }
}
