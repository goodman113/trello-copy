package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.AuthProvider;
import my.project.trellocopy.entity.enums.UserRole;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String avatarUrl;
    private LocalDateTime lastSeen;
    private String providerId;

}
