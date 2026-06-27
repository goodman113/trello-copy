package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Board;
import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.projection.BoardColumnProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> getBoardColumnsByBoard(Board board);

    @Query("""
        select bc.id as id,
               bc.title as columnTitle,
               bc.position as position,
               t.id as taskId,
               t.title as taskTitle,
               t.description as taskDescription,
               t.priority as priority,
               t.taskStatus as status,
               t.assignedTo.id as userId,
               t.assignedTo.username as username,
               t.assignedTo.avatarUrl as userAvatar,
               t.dueDate as dueDate,
               t.boardColumn.id as boardColumnId,
               t.position as taskPosition,
               count(distinct c.id) as commentCount
        from BoardColumn bc
        left join Task t on t.boardColumn = bc
        left join Comment c on c.task = t
        where bc.board.id = :boardId
        group by bc.id, bc.title, bc.position,
                              t.id, t.title, t.description, t.priority, t.taskStatus,
                              t.assignedTo.id, t.assignedTo.username, t.assignedTo.avatarUrl,
                              t.dueDate, t.boardColumn.id, t.position
        order by bc.position,t.position

""")
    List<BoardColumnProjection> getBoardColumnByBoard(Long boardId);

    @Query("""
    select max(bc.position)
    from BoardColumn bc
    where bc.board = :boardById
""")
    Long findMaxPosition(Board boardById);
}
