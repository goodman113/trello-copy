package my.project.trellocopy.controller;

import com.nimbusds.oauth2.sdk.Response;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.request.BoardColumnCreateRequest;
import my.project.trellocopy.entity.request.BoardColumnPositionUpdateRequest;
import my.project.trellocopy.entity.request.BoardColumnUpdateRequest;
import my.project.trellocopy.entity.request.BoardReorderRequest;
import my.project.trellocopy.service.BoardColumnService;
import my.project.trellocopy.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/columns")
public class BoardColumnController {

    private final BoardColumnService boardColumnService;
    private final BoardService boardService;

    @PutMapping("/{columnId}")
    public ResponseEntity<?> updateBoardColumn(@PathVariable Long columnId, @RequestBody BoardColumnUpdateRequest boardColumnUpdateRequest) {
        return ResponseEntity.ok(boardColumnService.updateBoardColumn(columnId, boardColumnUpdateRequest));
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<?> deleteBoardColumn(@PathVariable Long columnId) {
        return ResponseEntity.ok(boardColumnService.delete(columnId));
    }

    @PutMapping("/reorder")
    public ResponseEntity<?> updateBoardColumnPosition(@RequestBody BoardReorderRequest request) {
        return ResponseEntity.ok(boardColumnService.updateBoardColumnPosition(request.columns()));
    }
}
