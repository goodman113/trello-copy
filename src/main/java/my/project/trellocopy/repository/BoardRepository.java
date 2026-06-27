package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Board;
import my.project.trellocopy.entity.Workspace;
import my.project.trellocopy.entity.projection.BoardByWorkspaceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("""
        select  b.id,
                b.name as title,
                b.description,
                b.color,
                count(distinct t.id) as taskCount,
                b.createdAt
            from Board b
        join BoardColumn bc on bc.board = b
        join Task t on t.boardColumn = bc
        where b.workspace = :workspace
""")
    List<BoardByWorkspaceProjection> getBoardsByWorkspace(Workspace workspace);
}
