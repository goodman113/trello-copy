package my.project.trellocopy.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.repository.UserRepository;
import my.project.trellocopy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    final UserService userService;

    @GetMapping("/online")
    public ResponseEntity<?> getOnlineUsers(@RequestParam Long workspaceId) {
        return ResponseEntity.ok(userService.getAllUsersByWorkspaceAndOnline(workspaceId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getMe(currentUser));
    }
}
