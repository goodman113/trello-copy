package my.project.trellocopy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.project.trellocopy.entity.base.BaseEntity;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace extends BaseEntity {
    private String name;
    private String description;
    private String logoUrl;
    private String color; // auto generated color palette 8 hex for workspace
    @ManyToOne
    private User owner;

}
