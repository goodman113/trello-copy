package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.request.MemberRoleChangeRequest;
import my.project.trellocopy.entity.request.SendInvitationRequest;
import my.project.trellocopy.entity.request.WorkspaceCreateRequest;
import my.project.trellocopy.entity.response.WorkspaceResponse;
import my.project.trellocopy.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    final WorkspaceService workspaceService;

    @GetMapping
    public ResponseEntity<Map<String , List<WorkspaceResponse>>> getWorkspaces(@CurrentUser User currentUser) {
        return ResponseEntity.ok(Map.of("workspaces",workspaceService.getWorkspaces(currentUser)));
    }

    @PostMapping("/workspaces")
    public ResponseEntity<?> createWorkspace(WorkspaceCreateRequest workspaceRequest,
                                             @CurrentUser User currentUser) {
        WorkspaceResponse workspace = workspaceService.createWorkspace(workspaceRequest,currentUser);

        return ResponseEntity.ok(workspace);
    }

    @GetMapping("/workspaces/{id}/boards")
    public ResponseEntity<?> getBoardsByWorkspaceId(@CurrentUser User currentUser, Long id) {
        return ResponseEntity.ok(workspaceService.getBoardsByWorkspaceId(currentUser, id));
    }



    @GetMapping("/{workspaceId}/users/search")
    public ResponseEntity<?> getWorkspaceUsers(@CurrentUser User currentUser, @PathVariable Long workspaceId,
                                               @RequestParam String q) {
        return ResponseEntity.ok(Map.of("users",workspaceService.searchWorkspaceUsers(currentUser, workspaceId, q)));
    }

    @PostMapping("/{workspaceId}/invitations")
    public ResponseEntity<?> sendInvite(@RequestBody SendInvitationRequest request,
                                        @PathVariable Long workspaceId,
                                        @CurrentUser User user){
        return ResponseEntity.ok(workspaceService.sendInvitation(request,workspaceId, user));
    }
    @GetMapping("/{workspaceId}/invitations")
    public ResponseEntity<?> getAllInvitations(@CurrentUser User currentUser, @PathVariable Long workspaceId) {
        return ResponseEntity.ok(Map.of("invitations", workspaceService.getAllInvitations(currentUser,workspaceId)));
    }

    @DeleteMapping("/{workspaceId}/invitations/{invitationId}")
    public ResponseEntity<?> cancelInvitation(@PathVariable Long invitationId,
                                              @PathVariable Long workspaceId){
        return ResponseEntity.ok(workspaceService.cancelInvitation(invitationId,workspaceId));
    }

    @PutMapping("/{workspaceId}/members/{memberId}/role")
    public ResponseEntity<?> changeRole(@PathVariable Long memberId,
                                        @PathVariable Long workspaceId,
                                        @RequestBody MemberRoleChangeRequest request,
                                        @CurrentUser User user){
        return ResponseEntity.ok(workspaceService.updateMemberRole(memberId,workspaceId, request, user));
    }



    @GetMapping("/{workspaceId}/members")
    public ResponseEntity<?> getWorkspaceMembers(@CurrentUser User currentUser, @PathVariable Long workspaceId) {
        return ResponseEntity.ok(Map.of("members", workspaceService.getWorkspaceMembers(currentUser, workspaceId)));
    }

    @DeleteMapping("/{workspaceId}/members/{memberId}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long memberId, @PathVariable Long workspaceId,
                                          @CurrentUser User user){
        return ResponseEntity.ok(workspaceService.deleteWorker(memberId,workspaceId,user));
    }





}

