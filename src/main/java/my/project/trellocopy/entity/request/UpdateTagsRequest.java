package my.project.trellocopy.entity.request;

import java.util.List;

public record UpdateTagsRequest(
        List<String> tags
) {
}
