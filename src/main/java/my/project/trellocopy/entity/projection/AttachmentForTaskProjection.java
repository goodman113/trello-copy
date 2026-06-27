package my.project.trellocopy.entity.projection;

import java.time.LocalDateTime;

public interface AttachmentForTaskProjection {
    Long getId();
    String getName();
    String getFileUrl();
    Double getSize();
    String getContentType();
    LocalDateTime getCreatedAt();
}
