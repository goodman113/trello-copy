package my.project.trellocopy.repository;

import my.project.trellocopy.entity.Attachment;
import my.project.trellocopy.entity.projection.AttachmentForTaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("""
        select a.id as id,
               a.fileName as name,
               a.fileUrl as fileUrl,
               a.fileSize as size,
               a.contentType as contentType,
               a.createdAt as createdAt
        from Attachment a
        where a.task.id = :taskId and a.deleted = false
    """)
    List<AttachmentForTaskProjection> findAttachmentsByTaskId(Long taskId);
}
