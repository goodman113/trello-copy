package my.project.trellocopy.controller;

import com.nimbusds.oauth2.sdk.Response;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.config.CurrentUser;
import my.project.trellocopy.entity.Attachment;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.request.*;
import my.project.trellocopy.service.ActivityLogService;
import my.project.trellocopy.service.AttachmentService;
import my.project.trellocopy.service.CommentService;
import my.project.trellocopy.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    final TaskService taskService;
    private final CommentService commentService;
    final AttachmentService attachmentService;
    final ActivityLogService activityLogService;

    @GetMapping("{workspaceId}")
    public ResponseEntity<?> getTasks(@PathVariable String workspaceId) {
        return ResponseEntity.ok(taskService.getTasks(workspaceId));
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, UpdateTaskStatus status,
                                          @CurrentUser User user) {
        return ResponseEntity.ok(taskService.updateStatus(id, status,user));
    }

    @PostMapping
    public ResponseEntity<?> create(TaskCreateRequest request, @CurrentUser User user) {
        return ResponseEntity.ok(taskService.create(request, user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.delete(id));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, TaskUpdateRequest request,
                                        @CurrentUser User user){
        return ResponseEntity.ok(taskService.updateTask(taskId, request,user));
    }

    @PutMapping("/reorder")
    public ResponseEntity<?> taskUpdatePosition(@RequestBody TaskReorderRequest request) {
        return ResponseEntity.ok(taskService.updateTaskPosition(request.tasks()));
    }


    @GetMapping("/details/{taskId}")
    public ResponseEntity<?> getTaskDetails(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskDetails(taskId));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<?> getTaskComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getTaskComments(taskId));
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long taskId,
                                           @RequestBody CommentCreateRequest request,
                                           @CurrentUser User user) {
        return ResponseEntity.ok(commentService.createComment(taskId, request,user));
    }
    @PutMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long taskId,
                                           @PathVariable Long commentId,
                                           @RequestBody CommentUpdateRequest request,
                                           @CurrentUser User user) {
        return ResponseEntity.ok(commentService.updateComment(taskId, commentId, request, user));
    }

    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long taskId,
                                           @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(taskId, commentId));
    }

    @GetMapping("/{taskId}/attachments")
    public ResponseEntity<?> getTaskAttachments(@PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentService.getAttachmentsForTask(taskId));
    }

    @PostMapping("/{taskId}/attachments")
    public ResponseEntity<?> createAttachment(@PathVariable Long taskId,
                                              @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachmentService.createAttachment(taskId, file));
    }

    @DeleteMapping("/{taskId}/attachments/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long taskId,
                                              @PathVariable Long attachmentId) {
        return ResponseEntity.ok(attachmentService.deleteAttachment(taskId, attachmentId));
    }

    @GetMapping("/{taskId}/activity")
    public ResponseEntity<?> getActivities(@PathVariable Long taskId) {
        return ResponseEntity.ok(Map.of("activities",activityLogService.getTaskActivities(taskId)));
    }

    @PutMapping("/{taskId}/tags")
    public ResponseEntity<?> updateTags(@PathVariable Long taskId, @RequestBody UpdateTagsRequest request) {
        return ResponseEntity.ok(taskService.updateTags(taskId, request));
    }

    @GetMapping("/{taskId}/presence")
    public ResponseEntity<?> getPresence(@PathVariable Long taskId) {
        return ResponseEntity.ok(Map.of("viewers",taskService.getPresence(taskId)));
    }
}
