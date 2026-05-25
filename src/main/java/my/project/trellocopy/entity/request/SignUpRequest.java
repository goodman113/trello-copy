package my.project.trellocopy.entity.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
}
