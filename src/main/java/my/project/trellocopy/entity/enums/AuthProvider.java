package my.project.trellocopy.entity.enums;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuthProvider extends BaseEntity {
    private String provider;
    private String providerUserId;
    private Long userId;
}
