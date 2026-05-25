package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;

@Entity
@Table(name = "attachments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment extends BaseEntity {
    private String fileName;

    private String fileUrl;

    private Long fileSize;

    private String contentType;

    @ManyToOne
    private Task task;
}