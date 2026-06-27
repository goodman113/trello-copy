package my.project.trellocopy.entity.response;

import java.util.List;

public record BoardColumnCreateResponse(
        Long id,
        String title,
        Long position,
        List<TaskWorkspaceResponse> tasks
) {
}
