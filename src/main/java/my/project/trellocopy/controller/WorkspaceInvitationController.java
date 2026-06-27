package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.service.WorkspaceService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invitations")
public class WorkspaceInvitationController {
    final WorkspaceService workspaceService;

    @GetMapping("{token}")
    public ResponseEntity<?> getInvitation(@PathVariable String token) {
        return ResponseEntity.ok(workspaceService.getInvitation(token));
    }

    @PostMapping("{token}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable String token, @CurrentUser User currentUser) throws BadRequestException {
        return ResponseEntity.ok(workspaceService.acceptInvitation(token,currentUser));
    }
    @PostMapping("{token}/decline")
    public ResponseEntity<?> declineInvitation(@PathVariable String token, @CurrentUser User currentUser) throws BadRequestException {
        return ResponseEntity.ok(workspaceService.declineInvitation(token,currentUser));
    }
}
