package my.project.trellocopy.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserCreate {
    private String email;
    private String password;
    private String fullName;
    private String provider;
    private String picture;
    private String providerId;
}
