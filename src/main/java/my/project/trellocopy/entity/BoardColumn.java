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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoardColumn extends BaseEntity {

    private String title;

    private Long position;

    @ManyToOne
    private Board board;

    private Boolean archived;
}
