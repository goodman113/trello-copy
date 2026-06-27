package my.project.trellocopy.entity.request;

import java.util.List;

public record TaskReorderRequest(
        List<TaskUpdatePositionRequest> tasks
) {
}
