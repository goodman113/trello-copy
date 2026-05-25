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
    USER_ALREADY_EXIST("user.already.exist", HttpStatus.CONFLICT ),;


    private final String msg;
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorType(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

}
