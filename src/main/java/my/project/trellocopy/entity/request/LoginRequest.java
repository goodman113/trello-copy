package my.project.trellocopy.entity.request;

public record LoginRequest (
        String email,
        String password,
        boolean remember
) {

}
