package my.project.trellocopy.config;

import com.sun.security.auth.UserPrincipal;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.prop.AppProp;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Lazy
    final UserRepository userRepository;
    final AppProp prop;

    @Bean
    @Lazy
    public UserDetailsService getUserDetailsService() {
        return
                username ->{
                        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> RestException.restThrow(ErrorType.USER_NOT_FOUND));
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build();
        };
    }


    @Bean
    public MinioClient getMinioClient() {
        return  MinioClient.builder()
                .endpoint(prop.getAwsS3().getEndpoint())
                .credentials(prop.getAwsS3().getAccessKey(), prop.getAwsS3().getSecretKey())
                .build();
    }

}
