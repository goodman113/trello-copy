package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Board;
import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.Task;
import my.project.trellocopy.entity.dto.BoardCreateRequest;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.projection.BoardColumnProjection;
import my.project.trellocopy.entity.request.BoardUpdateRequest;
import my.project.trellocopy.entity.response.BoardColumnCreateResponse;
import my.project.trellocopy.entity.response.BoardCreateResponse;
import my.project.trellocopy.entity.response.TaskWorkspaceResponse;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.BoardColumnRepository;
import my.project.trellocopy.repository.BoardRepository;
import my.project.trellocopy.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final WorkspaceService workspaceService;
    final BoardRepository boardRepository;
    final BoardColumnRepository boardColumnRepository;
    final TaskRepository taskRepository;

    public Object create(BoardCreateRequest boardRequest) {
        Board board = new Board();
        if (boardRequest == null) {
            return Map.of("message", "Board request is null");
        }
        board.setName(boardRequest.title());
        board.setDescription(boardRequest.description());
        board.setWorkspace(workspaceService.getWorkspaceById(boardRequest.workspaceId()));
        board.setColor(boardRequest.color() == null ? workspaceService.colorGenerator() : boardRequest.color());
        board.setBackgroundUrl(workspaceService.getAvatarUrl(board.getName()));
        Board save = boardRepository.save(board);

        return new BoardCreateResponse(save.getId(), save.getName(), save.getColor(), 0, save.getCreatedAt());
    }

    public Object update(BoardUpdateRequest boardRequest, Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> RestException.restThrow(ErrorType.BOARD_NOT_FOUND));
        if (boardRequest.title() != null) {
            board.setName(boardRequest.title());
        }
        if (boardRequest.description() != null) {
            board.setDescription(boardRequest.description());
        }
        if (boardRequest.color() != null) {
            board.setColor(boardRequest.color());
        }
        Board save = boardRepository.save(board);
        List<BoardColumn> boardColumn = boardColumnRepository.getBoardColumnsByBoard(board);
        List<Task> tasks = taskRepository.findTasksByBoardColumns(boardColumn);
        return new BoardCreateResponse(save.getId(), save.getName(), save.getColor(), tasks.size(), save.getCreatedAt());
    }

    public Object delete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> RestException.restThrow(ErrorType.BOARD_NOT_FOUND));
        boardRepository.delete(board);
        return Map.of("message", "Board deleted successfully");
    }

    public Object getBoardColumnByBoard(Long boardId) {
        List<BoardColumnProjection> boardColumnProjection = boardColumnRepository.getBoardColumnByBoard(boardId);
        Map<Long, List<BoardColumnProjection>> collect = boardColumnProjection.stream().collect(Collectors.groupingBy(BoardColumnProjection::getId));

        return collect.entrySet().stream().map(entry -> {
            Long boardColumnId = entry.getKey();
            List<BoardColumnProjection> value = entry.getValue();
            return new BoardColumnCreateResponse(
                    boardColumnId,
                    value.getFirst().getColumnTitle(),
                    value.getFirst().getPosition(),
                    value.stream()
                            .filter(task -> task.getTaskId() != null)
                            .map(task -> new TaskWorkspaceResponse(
                            task.getTaskId(),
                            task.getTaskTitle(),
                            task.getStatus(),
                            task.getPriority(),
                            new UserDtoResponse(
                                    task.getUserId(),
                                    task.getUsername(),
                                    task.getUserAvatar()
                            ),
                            task.getDueDate(),
                            task.getBoardColumnId(),
                            task.getColumnTitle(),
                            task.getCommentCount()
                    )).toList()
            );
        });

    }

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> RestException.restThrow(ErrorType.BOARD_NOT_FOUND));
    }
}