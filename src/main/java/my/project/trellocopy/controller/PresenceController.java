package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.service.PresenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/presence")
public class PresenceController {

    private final PresenceService presenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> checkPresence(@PathVariable Long userId) {
        boolean isOnline = presenceService.isOnline(userId);
        return ResponseEntity.ok(isOnline);
    }
}
