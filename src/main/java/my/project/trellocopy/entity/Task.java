package my.project.trellocopy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.TaskPriority;
import my.project.trellocopy.entity.enums.TaskStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends BaseEntity {

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    private User assignedTo;

    @ManyToOne
    private BoardColumn boardColumn;

    private LocalDateTime dueDate;

    private String position;

    private Boolean archived;

    @Version
    private Long version;

    private TaskStatus taskStatus;
}
