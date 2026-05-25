package my.project.trellocopy.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {

    private final Boolean success;

    private String message;

    private T data;

    public ApiResponse(Boolean success) {
        this.success = success;
    }

    private ApiResponse(T data, Boolean success) {
        this.data = data;
        this.success = success;
    }


    private ApiResponse(T data, Boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    private ApiResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public static <E> ApiResponse<E> success(E data) {
        return new ApiResponse<>(data, Boolean.TRUE);
    }

    public static <E> ApiResponse<E> success(E data, String message) {
        return new ApiResponse<>(data, Boolean.TRUE, message);
    }

    public static <E> ApiResponse<E> success() {
        return new ApiResponse<>(Boolean.TRUE);
    }

    public static ApiResponse<String> success(String message) {
        return new ApiResponse<>(message, Boolean.TRUE);
    }

    public static ApiResponse<String> error(String errorMsg) {
        return new ApiResponse<>(errorMsg, Boolean.FALSE);
    }

    public static ApiResponse<ErrorResponse> error(String errorMsg, Integer errorCode, String errorPath) {
        return new ApiResponse<>(new ErrorResponse(errorMsg, errorCode, errorPath), Boolean.FALSE);
    }

    public static ApiResponse<ErrorResponse> error(ErrorResponse data) {
        return new ApiResponse<>(data, Boolean.FALSE);
    }
}