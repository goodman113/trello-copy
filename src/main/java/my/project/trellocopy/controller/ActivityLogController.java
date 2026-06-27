package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityLogController {
    final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<?> activity(@RequestParam Long workspaceId) {
        return ResponseEntity.ok(Map.of("activities",activityLogService.getActivity(workspaceId)));
    }
}
