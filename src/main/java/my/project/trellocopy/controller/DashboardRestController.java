package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.service.BoardService;
import my.project.trellocopy.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DashboardRestController {
    final WorkspaceService workspaceService;
    final BoardService boardService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String query, @RequestParam(required = false) Long workspaceId) {
        return ResponseEntity.ok(workspaceService.search(query, workspaceId));
    }

}
