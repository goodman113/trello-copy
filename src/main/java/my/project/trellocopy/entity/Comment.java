package my.project.trellocopy.entity;

import jakarta.persistence.Column;
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
public class Comment extends BaseEntity {

    @Column(length = 3000)
    private String content;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User author;

    private Boolean edited;


}
