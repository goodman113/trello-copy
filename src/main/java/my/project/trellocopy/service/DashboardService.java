package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.request.WorkspaceCreateRequest;
import my.project.trellocopy.entity.response.WorkspaceResponse;
import my.project.trellocopy.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    final WorkspaceRepository workspaceRepository;

    public List<WorkspaceResponse> getWorkspaces(User user) {
        List<WorkspaceResponse> workspacesDatas = workspaceRepository.getWorkspacesDatas(user);

        return workspacesDatas;
    }

    public WorkspaceResponse createWorkspace(WorkspaceCreateRequest workspaceRequest) {

    }
}
