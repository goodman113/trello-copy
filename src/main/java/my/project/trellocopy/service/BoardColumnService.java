package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Board;
import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.request.BoardColumnCreateRequest;
import my.project.trellocopy.entity.request.BoardColumnPositionUpdateRequest;
import my.project.trellocopy.entity.request.BoardColumnUpdateRequest;
import my.project.trellocopy.entity.response.BoardColumnResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.BoardColumnRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardColumnService {
    final BoardColumnRepository boardColumnRepository;
    final BoardService boardService;
    public List<BoardColumn> getBoardColumnsByBoard(Board board) {
        return boardColumnRepository.getBoardColumnsByBoard(board);
    }

    public BoardColumn getBoardColumnById(Long aLong) {
        return boardColumnRepository.findById(aLong).orElseThrow(() -> RestException.restThrow(ErrorType.BOARDCOLUMN_NOT_FOUND));
    }

    public Object createBoardColumn(Long boardId, BoardColumnCreateRequest boardColumnCreateRequest) {
        if (boardColumnCreateRequest == null){
            return Map.of("message","request cant be null");
        }
        if (boardColumnCreateRequest.title() == null){
            return Map.of("message","title cant be null");
        }
        Board boardById = boardService.getBoardById(boardId);
        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setTitle(boardColumnCreateRequest.title());
        boardColumn.setBoard(boardById);
        boardColumn.setPosition(boardColumnRepository.findMaxPosition(boardById) + 1);
        BoardColumn save = boardColumnRepository.save(boardColumn);
        return new BoardColumnResponse(save.getId(),
                save.getTitle(),
                save.getPosition(),
                save.getBoard().getId());
    }

    public Object updateBoardColumn(Long columnId, BoardColumnUpdateRequest boardColumnUpdateRequest) {
        BoardColumn boardColumnById = getBoardColumnById(columnId);
        if (boardColumnUpdateRequest.title() != null){
            boardColumnById.setTitle(boardColumnUpdateRequest.title());
        }
        BoardColumn save = boardColumnRepository.save(boardColumnById);
        return new BoardColumnResponse(
                save.getId(),
                save.getTitle(),
                save.getPosition(),
                save.getBoard().getId());
    }

    public Object delete(Long columnId) {
        BoardColumn boardColumnById = getBoardColumnById(columnId);
        boardColumnRepository.delete(boardColumnById);
        return Map.of("message", "column deleted successfully");
    }

    public Object updateBoardColumnPosition(List<BoardColumnPositionUpdateRequest> boardColumnUpdateRequest) {
        for (BoardColumnPositionUpdateRequest request : boardColumnUpdateRequest) {
            BoardColumn boardColumnById = getBoardColumnById(request.boardColumnId());
            boardColumnById.setPosition(request.position());
            boardColumnRepository.save(boardColumnById);
        }
        return Map.of("message", "columns reordered successfully");
    }
}
