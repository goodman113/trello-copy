package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Notification;
import my.project.trellocopy.entity.User;
import my.project.trellocopy.entity.enums.NotificationType;
import my.project.trellocopy.entity.projection.NotificationFilterPropagation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        select count(n)
        from Notification n
        where n.receiver = :user
         and n.read = false
         and n.deleted = false
""")
    long countByUser(User user);


    @Query("""
        select n.id as id,
               n.type as type,
               n.title as title,
               n.message as message,
               n.read as read,
               n.createdAt as createdAt,
               n.actor.id as actorId,
               n.actor.username as actorName,
               n.actor.avatarUrl as avatarUrl,
               n.taskId as taskId,
               n.boardId as boardId,
               n.workspaceId as workspaceId
        from Notification n
        where n.receiver.id = :id
          and (:unread is null or n.read = :unread)
          and (:type is null or n.type = :type)
        order by n.createdAt desc
""")
    Page<NotificationFilterPropagation> findNotificationByFilters(Boolean unread, NotificationType type, Long id, Pageable pageable);

    @Modifying
    @Query("""
        update
    Notification n
        set n.read = true
        where n.receiver = :user
          and n.read = false
          and n.id = :id
          and n.deleted = false
""")
    Integer updateNotificationReadByUser(User user, Long id);

    @Modifying
    @Query("""
        update
    Notification n
        set n.read = true
        where n.receiver = :user
          and n.read = false
          and n.deleted = false
""")
    Integer updateNotificationAllReadByUser(User user);

    boolean existsNotificationById(Long id);
}
