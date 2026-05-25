package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.request.WorkspaceCreateRequest;
import my.project.trellocopy.entity.response.WorkspaceResponse;
import my.project.trellocopy.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DashboardRestController {
    final WorkspaceService workspaceService;

    @GetMapping("/workspaces")
    public ResponseEntity<Map<String , List<WorkspaceResponse>>> getWorkspaces(@CurrentUser User currentUser) {
        return ResponseEntity.ok(Map.of("workspaces",workspaceService.getWorkspaces(currentUser)));
    }

    @PostMapping("/workspaces")
    public ResponseEntity<?> createWorkspace(WorkspaceCreateRequest workspaceRequest) {
        WorkspaceResponse workspace = workspaceService.createWorkspace(workspaceRequest);

    }

}
