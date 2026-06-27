package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/deadlines")
@RestController
@RequiredArgsConstructor
public class DeadlineController {

    private final TaskService taskService;

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingDeadlines(@CurrentUser User user, @RequestParam(required = false) Long workspaceId) {
        return ResponseEntity.ok(taskService.getUpcomingDeadlines(user,workspaceId));
    }

}
