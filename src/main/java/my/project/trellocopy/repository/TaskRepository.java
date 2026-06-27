package my.project.trellocopy.repository;

import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.Task;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.projection.TaskDetailsProjection;
import my.project.trellocopy.entity.projection.TaskWorkspaceProjection;
import my.project.trellocopy.entity.projection.UpcomingDeadLinesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.boardColumn IN :boardColumn")
    List<Task> findTasksByBoardColumns(List<BoardColumn> boardColumn);

    @Query("""
        select t.id as id,
                t.title as title,
                t.taskStatus as status,
                t.priority as priority,
                t.dueDate as dueDate,
                t.assignedTo.id as userId,
                t.assignedTo.username as username,
                t.assignedTo.avatarUrl as avatar,
                bc.id as boardColumnId,
                bc.title as boardColumnName,
                count(distinct c.id) as commentCount
        from Task t
        join BoardColumn bc on bc = t.boardColumn
        join Board b on b = bc.board
        join Comment c on c.task = t
        where b.workspace.id = :workspaceId
        group by t.id,t.title, t.taskStatus, t.priority,t.dueDate,
                 t.assignedTo.id, t.assignedTo.username,t.assignedTo.avatarUrl,
                 bc.id,bc.title
        order by bc.position, t.position
""")
    List<TaskWorkspaceProjection> getTaskByWorkspace(String workspaceId);

    @Query("""
        select t.id as id,
                t.title as title,
                t.dueDate as dueDate,
                bc.board.name as boardName,
                t.assignedTo.id as userId,
                t.assignedTo.username as username,
                t.assignedTo.avatarUrl as avatar
        from Task t
        join BoardColumn bc on bc = t.boardColumn
        join Board b on b = bc.board
        where  (:worspaceId is null or b.workspace.id = :workspaceId)
        and t.assignedTo = :user
""")
    List<UpcomingDeadLinesProjection> getUpcomingDeadlines(User user, Long workspaceId);




    @Query("""
        select t.id as id,
                t.title as title,
                t.description as description,
                t.priority as priority,
                t.taskStatus as taskStatus,
                t.dueDate as dueDate,
                
                t.assignedTo.id as userId,
                t.assignedTo.username as username,
                t.assignedTo.avatarUrl as avatar,
                
                bc.id as columnId,
                bc.title as columnTitle,
                
                b.id as boardId,
                b.name as boardTitle,
                
                t.createdBy as createBy,
                t.createdAt as createdAt,
                t.updatedAt as updatedAt,
                t.tags as tags,
                w.id as workspaceId
                
        from Task t
        join BoardColumn bc on bc = t.boardColumn
        join Board b on b = bc.board
        join Workspace w on w = b.workspace
        where t.id = :taskId
""")
    TaskDetailsProjection findByIdAndPlusDatas(Long taskId);


}
