package my.project.trellocopy.entity.request;

import java.util.List;

public record BoardReorderRequest(
        List<BoardColumnPositionUpdateRequest> columns
) {
}
