package my.project.trellocopy.entity.response;

import java.time.LocalDateTime;

public record CommentDetailResponse(
        Long id,
        String content,
        Boolean edited,
        UserDtoResponse author,
        LocalDateTime createdAt
) {
}
