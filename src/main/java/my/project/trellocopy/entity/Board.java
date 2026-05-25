package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Board extends BaseEntity {
    private String name;
    private String description;
    private String backgroundUrl;
    private String color; //auto generated color palette 8 hex for workspace

    @ManyToOne
    private Workspace workspace;

}
