package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_presence")
public class UserPresence {

    @Id
    private Long userId;

    private Boolean online;

    private LocalDateTime lastSeen;
}