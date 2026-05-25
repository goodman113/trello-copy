package my.project.trellocopy.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import my.project.trellocopy.entity.enums.ErrorType;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class RestException extends RuntimeException {
    private HttpStatus httpStatus;
    private final ErrorType errorType;

    public RestException(ErrorType errorType) {
        this.errorType = errorType;
        this.httpStatus =errorType.getStatus();
    }

    private RestException(@NotNull ErrorType errorType, HttpStatus status) {
        this.errorType = errorType;
        this.httpStatus = status;
    }

    public static RestException restThrow(@NotNull ErrorType errorType) {
        return new RestException(errorType);
    }

    public static RestException restThrow(@NotNull ErrorType errorType, HttpStatus status) {
        return new RestException(errorType, status);
    }
}
