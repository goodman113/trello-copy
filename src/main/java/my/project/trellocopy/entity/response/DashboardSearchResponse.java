package my.project.trellocopy.entity.response;

import java.util.List;

public record DashboardSearchResponse(
        List<BoardDtoResponse> boards,
        List<TaskDtoResponse> tasks,
        List<WorkspaceWorkerDtoResponse> members
) {
}
