package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Board;
import my.project.trellocopy.entity.BoardColumn;
import my.project.trellocopy.entity.Task;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.enums.NotificationType;
import my.project.trellocopy.entity.enums.TaskPriority;
import my.project.trellocopy.entity.enums.TaskStatus;
import my.project.trellocopy.entity.projection.TaskDetailsProjection;
import my.project.trellocopy.entity.projection.TaskWorkspaceProjection;
import my.project.trellocopy.entity.projection.UpcomingDeadLinesProjection;
import my.project.trellocopy.entity.request.*;
import my.project.trellocopy.entity.response.*;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class TaskService {
    final TaskRepository taskRepository;
    final BoardColumnService boardColumnService;
    final UserService userService;
    final CommentService commentService;
    final TaskPresenceService presenceService;
    final NotificationService notificationService;

    public List<Task> getTaskByBoard(Board save) {
        List<BoardColumn> boardColumn = boardColumnService.getBoardColumnsByBoard(save);
        return taskRepository.findTasksByBoardColumns(boardColumn);
    }

    public Object getTasks(String workspaceId) {
        List<TaskWorkspaceProjection> taskWorkspaceProjection = taskRepository.getTaskByWorkspace(workspaceId);
        Stream<TaskWorkspaceResponse> taskResponse = taskWorkspaceProjection.stream().map(task -> new TaskWorkspaceResponse(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getPriority(),
                new UserDtoResponse(
                        Long.parseLong(task.getUserId()),
                        task.getUsername(),
                        task.getAvatar()),
                LocalDateTime.parse(task.getDueDate()),
                task.getBoardColumnId(),
                task.getBoardColumnName(),
                task.getCommentCount()
        ));
        return Map.of("tasks", taskResponse);
    }

    public Object updateStatus(Long id, UpdateTaskStatus status,User user) {
        Task task = taskRepository.findById(id).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        task.setTaskStatus(TaskStatus.valueOf(status.status().toUpperCase()));
        Task save = taskRepository.save(task);
        if (save.getAssignedTo()!=null) {
            notificationService.createAndPush(user,
                    task.getAssignedTo(),
                    "Status changed",
                    user.getUsername() + " moved \"" + task.getTitle() + "\" to " + status.status(),
                    NotificationType.TASK_UPDATED,
                    task.getId(),task.getBoardColumn().getBoard().getId(),
                    task.getBoardColumn().getBoard().getWorkspace().getId()
            );
        }
        return Map.of("id", save.getId(),
                    "status", save.getTaskStatus());
    }

    public Object create(TaskCreateRequest request,User user) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setTaskStatus(TaskStatus.valueOf(request.status().toUpperCase()));
        task.setPriority(TaskPriority.valueOf(request.priority().toUpperCase()));
        task.setDueDate(request.dueDate());
        task.setBoardColumn(boardColumnService.getBoardColumnById(request.boardColumnId()));
        task.setAssignedTo(userService.getUserById(request.assigneeId()));
        Task save = taskRepository.save(task);
        if (save.getAssignedTo() !=null){
            notificationService.createAndPush(user,save.getAssignedTo(),"Task Assigned",
                    "You have been assigned a new task: "+save.getTitle(),
                    NotificationType.TASK_ASSIGNED,
                    save.getId(),save.getBoardColumn().getBoard().getId(),
                    save.getBoardColumn().getBoard().getWorkspace().getId());
        }
        return save;
    }

    public Object delete(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        taskRepository.delete(task);
        return Map.of("message", "Task deleted successfully");
    }

    public Object getUpcomingDeadlines(User user, Long workspaceId) {
        List<UpcomingDeadLinesProjection> deadLinesProjections = taskRepository.getUpcomingDeadlines(user,workspaceId);
        List<TaskUpcomingDeadlineResponse> list = deadLinesProjections.stream().map(p -> new TaskUpcomingDeadlineResponse(
                p.getId(),
                p.getTitle(),
                p.getDueDate(),
                p.getBoardName(),
                new UserDtoResponse(
                        p.getUserId(),
                        p.getUsername(),
                        p.getAvatar()
                )
        )).toList();
        return Map.of("deadlines", list);
    }

    public Object updateTask(Long taskId, TaskUpdateRequest request,User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        if (request.title() != null){
            task.setTitle(request.title());
        }
        if (request.description() != null){
            task.setDescription(request.description());
        }
        if (request.priority() != null){
            task.setPriority(TaskPriority.valueOf(request.priority().toUpperCase()));
        }
        if (request.dueDate() != null){
            task.setDueDate(request.dueDate());
        }
        if (request.assigneeId() != null){
            User userByAssigneeId = userService.getUserById(request.assigneeId());
            if (request.assigneeId().equals(task.getAssignedTo().getId())) {
                notificationService.createAndPush(user,
                        userByAssigneeId,
                        "Task Assigned",
                        "You have been assigned a new task: "+task.getTitle(),
                        NotificationType.TASK_ASSIGNED,
                        task.getId(),task.getBoardColumn().getBoard().getId(),
                        task.getBoardColumn().getBoard().getWorkspace().getId()
                        );
            }
            task.setAssignedTo(userByAssigneeId);
        }
        if (request.boardColumnId() != null){
            task.setBoardColumn(boardColumnService.getBoardColumnById(request.boardColumnId()));
        }
        Task save = taskRepository.save(task);
        return new TaskWorkspaceResponse(
                save.getId(),
                save.getTitle(),
                save.getTaskStatus().name(),
                save.getPriority().name(),
                new UserDtoResponse(
                        save.getAssignedTo().getId(),
                        save.getAssignedTo().getUsername(),
                        save.getAssignedTo().getAvatarUrl()
                ),
                save.getDueDate(),
                save.getBoardColumn().getId(),
                save.getBoardColumn().getTitle(),
                commentService.getCommentsCountByTask(task)
        );
    }

    public Object updateTaskPosition(List<TaskUpdatePositionRequest> tasks) {
        for (TaskUpdatePositionRequest task : tasks) {
            Task taskById = taskRepository.findById(task.taskId()).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
            taskById.setPosition(task.position());
            taskRepository.save(taskById);
        }
        return Map.of("message", "Tasks position updated successfully");
    }

    public Object getTaskDetails(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            return RestException.restThrow(ErrorType.TASK_NOT_FOUND);
        }
        TaskDetailsProjection task = taskRepository.findByIdAndPlusDatas(taskId);
        return new TaskDetailsResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getTaskStatus(),
                task.getDueDate(),
                new UserDtoResponse(
                        task.getUserId(),
                        task.getUsername(),
                        task.getAvatar()
                ),
                new BoardColumnTaskDetailsResponse(
                        task.getColumnId(),
                        task.getColumnTitle()
                ),
                new BoardTaskDetailsResponse(
                        task.getBoardId(),
                        task.getBoardTitle()
                ),
                task.getCreatedBy(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getTags(),
                task.getWorkspaceId()
        );
    }

    public Object updateTags(Long taskId, UpdateTagsRequest request) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        task.getTags().clear();
        task.setTags(request.tags());
        Task save = taskRepository.save(task);
        return Map.of(
                "tags", save.getTags());
    }

    public Object getPresence(Long taskId) {
        List<Map<String, Object>> viewers = presenceService.getViewers(taskId);
        return viewers.stream().map(v -> new UserDtoResponse(
                Long.parseLong(v.get("id").toString()),
                v.get("name").toString(),
                v.get("avatarUrl").toString()
        )).collect(Collectors.toList());
    }
}
