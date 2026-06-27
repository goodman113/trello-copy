package my.project.trellocopy.service;

import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Comment;
import my.project.trellocopy.entity.Task;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.enums.NotificationType;
import my.project.trellocopy.entity.request.CommentCreateRequest;
import my.project.trellocopy.entity.request.CommentUpdateRequest;
import my.project.trellocopy.entity.response.CommentDetailResponse;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.CommentRepository;
import my.project.trellocopy.repository.TaskRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommentService {
    final CommentRepository commentRepository;
    final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;


    public Integer getCommentsCountByTask(Task task) {
        return commentRepository.countByTask(task);
    }

    public Object getTaskComments(Long taskId) {
        List<Comment> allByTaskId = commentRepository.findAllByTaskId(taskId);
        return allByTaskId.stream().map(CommentService::convertCommentToDto
        ).toList();
    }

    private static @NonNull CommentDetailResponse convertCommentToDto(Comment comment) {
        return new CommentDetailResponse(
                comment.getId(),
                comment.getContent(),
                comment.getEdited(),
                new UserDtoResponse(
                        comment.getAuthor().getId(),
                        comment.getAuthor().getUsername(),
                        comment.getAuthor().getAvatarUrl()
                ),
                comment.getCreatedAt()
        );
    }

    public Object createComment(Long taskId, CommentCreateRequest request, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setEdited(false);
        comment.setAuthor(user);
        comment.setTask(task);
        Comment save = commentRepository.save(comment);

        String content = save.getContent();
        Pattern pattern = Pattern.compile("@(\\S+)");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            String username = matcher.group(1);
            userService.getByUsername(username).ifPresent(mentionedUser -> {
                notificationService.createAndPush(user,
                        mentionedUser,
                        "Mentioned in comment",
                        user.getUsername()+" mentioned you a comment on task: "+task.getTitle(),
                        NotificationType.MENTIONED_IN_COMMENT,
                        task.getId(),task.getBoardColumn().getBoard().getId(),
                        task.getBoardColumn().getBoard().getWorkspace().getId()
                );
            });
        }
        if (task.getAssignedTo()!=null&& !task.getAssignedTo().getId().equals(user.getId())) {
            notificationService.createAndPush(user,
                    task.getAssignedTo(),
                    "New Comment",
                    user.getUsername()+" commented on \" "+task.getTitle() + "\"",
                    NotificationType.MENTIONED_IN_COMMENT,
                    task.getId(),task.getBoardColumn().getBoard().getId(),
                    task.getBoardColumn().getBoard().getWorkspace().getId()
            );
        }

        return convertCommentToDto(save);



    }

    public Object updateComment(Long taskId, Long commentId, CommentUpdateRequest request, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> RestException.restThrow(ErrorType.COMMENT_NOT_FOUND));
        if (!comment.getTask().getId().equals(task.getId())) {
            return Map.of("message","comment is not linked to this task");
        }
        comment.setContent(request.content());
        comment.setEdited(true);
        Comment save = commentRepository.save(comment);
        return convertCommentToDto(save);
    }

    public Object deleteComment(Long taskId, Long commentId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> RestException.restThrow(ErrorType.TASK_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> RestException.restThrow(ErrorType.COMMENT_NOT_FOUND));
        if (!comment.getTask().getId().equals(task.getId())) {
            return Map.of("message", "comment is not linked to this task");
        }
        comment.setDeleted(true);
        commentRepository.save(comment);
        return Map.of("message","comment has been deleted");

    }
}
