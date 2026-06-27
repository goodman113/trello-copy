package my.project.trellocopy.service;

import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.AuthProvider;
import my.project.trellocopy.entity.enums.UserRole;
import my.project.trellocopy.entity.request.LoginRequest;
import my.project.trellocopy.entity.request.SignUpRequest;
import my.project.trellocopy.entity.response.ApiResponse;
import my.project.trellocopy.repository.AuthProviderRepository;
import my.project.trellocopy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthProviderRepository authProviderRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authProviderRepository = authProviderRepository;
    }

    public ApiResponse<?> signUp(SignUpRequest signUpRequest) {
        boolean b = userRepository.existsUserByEmail(signUpRequest.getEmail());
        if (b) {
            return ApiResponse.success("User with email " + signUpRequest.getEmail() + " already exists");
        }
        boolean b1 = userRepository.existsUserByUsername(signUpRequest.getUsername());
        if (b1) {
            return ApiResponse.success("User with username " + signUpRequest.getUsername() + " already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        AuthProvider authProvider = new AuthProvider();
        authProvider.setProvider("local");
        authProvider.setProviderUserId(null);
        user.setFullName(signUpRequest.getFullName());
        user.setRole(UserRole.USER);
        User save = userRepository.save(user);
        authProvider.setUserId(save.getId());
        authProviderRepository.save(authProvider);
        return ApiResponse.success("User registered successfully");


    }

}
