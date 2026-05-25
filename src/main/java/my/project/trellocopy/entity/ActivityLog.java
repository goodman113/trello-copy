package my.project.trellocopy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.ActivityType;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog extends BaseEntity {


    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String message;

    @ManyToOne
    private User actor;

    @ManyToOne
    private Task task;

    @ManyToOne
    private Board board;

}