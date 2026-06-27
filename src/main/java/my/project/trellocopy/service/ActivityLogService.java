package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.projection.ActivityLogByWorkspaceProjection;
import my.project.trellocopy.entity.response.ActivityLogByWorkspaceResponse;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

     final ActivityLogRepository activityLogRepository;
    public Object getActivity(Long workspaceId) {
        List<ActivityLogByWorkspaceProjection> projection = activityLogRepository.getActivityLogByWorkspace(workspaceId);
        return projection.stream().map(a ->
                new ActivityLogByWorkspaceResponse(
                        a.getId(),
                        a.getType(),
                        a.getMessage(),
                        new UserDtoResponse(a.getUserId(), a.getUsername(), a.getAvatar()),
                        a.getTimestamp()
                )
        ).toList();

    }

    public Object getTaskActivities(Long taskId) {
        List<ActivityLogByWorkspaceProjection> projection = activityLogRepository.getActivityLogByTask(taskId);
        return projection.stream().map(a ->
                new ActivityLogByWorkspaceResponse(
                        a.getId(),
                        a.getType(),
                        a.getMessage(),
                        new UserDtoResponse(a.getUserId(), a.getUsername(), a.getAvatar()),
                        a.getTimestamp()
                )
        ).toList();

    }
}
