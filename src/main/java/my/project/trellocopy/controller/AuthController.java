package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.request.LoginRequest;
import my.project.trellocopy.entity.request.SignUpRequest;
import my.project.trellocopy.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "auth/sign-up";
    }


    @ResponseBody
    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }


}
