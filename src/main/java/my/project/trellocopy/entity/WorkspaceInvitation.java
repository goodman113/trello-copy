package my.project.trellocopy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import my.project.trellocopy.entity.enums.InvitationStatus;
import my.project.trellocopy.entity.enums.WorkspaceWorkerRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkspaceInvitation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    private User inviter;

    private String inviteeEmail;

    @Enumerated(EnumType.STRING)
    private WorkspaceWorkerRole role;

    @Column(unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    private LocalDateTime expireAt;

    @PrePersist
    public void onCreate(){
        if (status==null){
            status = InvitationStatus.PENDING;
        }
        if (token==null){
            token = UUID.randomUUID().toString();
        }
        if (expireAt==null){
            expireAt = LocalDateTime.now().plusDays(5);
        }
    }

}
