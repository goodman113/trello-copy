package my.project.trellocopy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Setter
@Getter
public class Notification extends BaseEntity {


    @ManyToOne
    private User receiver;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Boolean read;

    @ManyToOne
    private User actor;

    private Long boardId;

    private Long workspaceId;

    private Long taskId;


}