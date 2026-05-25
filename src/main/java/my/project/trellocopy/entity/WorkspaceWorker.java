package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkspaceWorker extends BaseEntity {

    @ManyToOne
    private Workspace workspace;

    @ManyToOne
    private User worker;

    @Enumerated(EnumType.STRING)
    private WorkspaceWorkerRole workerRole;
}
