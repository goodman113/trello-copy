package my.project.trellocopy.entity.enums;

import lombok.Getter;
import org.hibernate.dialect.function.json.JsonTableSetReturningFunctionTypeResolver;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    INTERNAL_ERROR("internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_TYPE_ERROR("file.type.error", HttpStatus.BAD_REQUEST),
    ERROR_SAVING_FILE("error.saving.file", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_JSON("bad.json", HttpStatus.BAD_REQUEST), USER_NOT_FOUND("user.not.found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXIST("user.already.exist", HttpStatus.CONFLICT ),
    WORKSPACE_NOT_FOUND("workspace.not.found", HttpStatus.NOT_FOUND),
    WORKSPACE_NOT_FOUND_OR_IS_NOT_RELATED("workspace.not.found.or.is.not.related", HttpStatus.NOT_FOUND),
    BOARD_NOT_FOUND("board.not.found", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND("task.not.found", HttpStatus.NOT_FOUND),
    BOARDCOLUMN_NOT_FOUND("boardcolumn.not.found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("comment.not.found", HttpStatus.NOT_FOUND),
    ATTACHMENT_NOT_FOUND("attachment.not.found", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND("notification.not.found", HttpStatus.NOT_FOUND),
    INVITATION_NOT_FOUND("invitation.not.found", HttpStatus.NOT_FOUND),
    USER_IS_NOT_AUTHENTICATED("user.is.not.authenticated", HttpStatus.UNAUTHORIZED ),
    WORKER_NOT_FOUND("worker.not.found", HttpStatus.NOT_FOUND),
    USER_IS_NOT_OWNER_OF_WORKSPACE("user.is.not.owner.of.workspace", HttpStatus.FORBIDDEN ),;


    private final String msg;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorType(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

}
