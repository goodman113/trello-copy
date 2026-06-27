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
import java.util.ArrayList;
import java.util.List;

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

    private Long position;

    private Boolean archived;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Version
    private Long version;

    private TaskStatus taskStatus;
}
