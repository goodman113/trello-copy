package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Comment;
import my.project.trellocopy.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
        select count(distinct c.id)
        from Comment c
        where c.task = :task
""")
    Integer countByTask(Task task);


    @Query("""
        select c from Comment c where c.task.id = :taskId and c.deleted = false
""")
    List<Comment> findAllByTaskId(Long taskId);
}
