package my.project.trellocopy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.project.trellocopy.entity.Notification;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.ErrorType;
import my.project.trellocopy.entity.enums.NotificationType;
import my.project.trellocopy.entity.projection.NotificationFilterPropagation;
import my.project.trellocopy.entity.response.NotificationResponse;
import my.project.trellocopy.entity.response.UserDtoResponse;
import my.project.trellocopy.exception.RestException;
import my.project.trellocopy.repository.NotificationRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    final SimpMessagingTemplate messagingTemplate;



    public Object getCountForUser(User user) {
        long count = notificationRepository.countByUser(user);
        return Map.of("count", count);
    }

    public Object getNotificationsByFilter(User user, String filter, Integer page, Integer size) {
        // Implementation for fetching notifications by filter
        Pageable pageable = PageRequest.of(page, size);
        NotificationType type =null;
        Boolean unread =null;
        if (filter != null) {
            unread = !filter.equals("unread");
            if (unread) {
                type = NotificationType.valueOf(filter);
            }
        }
        Page<NotificationFilterPropagation> filterPropagation = notificationRepository.findNotificationByFilters(unread,type, user.getId(), pageable);
        return filterPropagation.map(n -> new NotificationResponse(
                n.getId(),
                n.getType(),
                n.getTitle(),
                n.getMessage(),
                n.getRead(),
                n.getCreatedAt(),
                new UserDtoResponse(n.getActorId(), n.getActorName(), n.getAvatarUrl()),
                n.getTaskId(),
                n.getBoardId(),
                n.getWorkspaceId()
        ));


    }

    private static @NonNull NotificationResponse toResponse(Notification n){
        return new NotificationResponse(
                n.getId(),
                n.getType().toString(),
                n.getTitle(),
                n.getMessage(),
                n.getRead(),
                n.getCreatedAt(),
                new UserDtoResponse(n.getActor().getId(), n.getActor().getUsername(), n.getActor().getAvatarUrl()),
                n.getTaskId(),
                n.getBoardId(),
                n.getWorkspaceId()
        );
    }

    @Transactional
    public Object readAllNotifications(User user) {
        Integer updateCount = notificationRepository.updateNotificationAllReadByUser(user);
        return Map.of("message", updateCount + " notifications were read" );
    }

    @Transactional
    public Object markReadNotificationByUser(User user, Long id) {
        boolean b = notificationRepository.existsNotificationById(id);
        if (!b) {
            return RestException.restThrow(ErrorType.NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.updateNotificationReadByUser(user,id);
        return Map.of("message", " notification"+ id +" was read" );
    }

    public NotificationResponse createAndPush(User actor, User receiver, String title, String message, NotificationType type, Long taskId, Long boardId, Long workspaceId) {
        // Create a new notification
        Notification n = new Notification();
        n.setActor(actor);
        n.setReceiver(receiver);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setTaskId(taskId);
        n.setBoardId(boardId);
        n.setRead(false);
        n.setWorkspaceId(workspaceId);

        notificationRepository.save(n);

        long count = notificationRepository.countByUser(receiver);
        NotificationResponse response = toResponse(n);
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + receiver.getId(),
                Optional.of(Map.of("type", "new_notification",
                        "notification", response,
                        "unreadCount", count)));


        return response;
    }
}
