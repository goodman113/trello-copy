package my.project.trellocopy.controller;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.dto.BoardCreateRequest;
import my.project.trellocopy.entity.request.BoardColumnCreateRequest;
import my.project.trellocopy.entity.request.BoardUpdateRequest;
import my.project.trellocopy.service.BoardColumnService;
import my.project.trellocopy.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/boards")
@RequiredArgsConstructor
@RestController
public class BoardController {
    final BoardService boardService;
    final BoardColumnService boardColumnService;


    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardCreateRequest boardRequest) {
        return ResponseEntity.ok(boardService.create(boardRequest));
    }

    @PutMapping("boards/{id}")
    public ResponseEntity<?> updateBoard(@RequestBody BoardUpdateRequest boardRequest, @PathVariable Long id) {
        return ResponseEntity.ok(boardService.update(boardRequest, id));
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {

        return ResponseEntity.ok(boardService.delete(id));
    }

    @GetMapping("/{boardId}/columns")
    public ResponseEntity<?> getBoardColumns(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardColumnByBoard(boardId));
    }

    @PostMapping("/{boardId}/columns")
    public ResponseEntity<?> createBoardColumn(@PathVariable Long boardId, @RequestBody BoardColumnCreateRequest boardColumnCreateRequest) {
        return ResponseEntity.ok(boardColumnService.createBoardColumn(boardId, boardColumnCreateRequest));
    }
}
