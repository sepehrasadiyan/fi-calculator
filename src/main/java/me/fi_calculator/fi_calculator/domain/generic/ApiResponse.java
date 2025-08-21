package me.fi_calculator.fi_calculator.domain.generic;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "ApiResponse", description = "Standard API response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int status,
        T data,
        String error,
        String message,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data, int status) {
        return new ApiResponse<>(true, status, data, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok(data, 200);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, 201, data, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return new ApiResponse<>(false, status, null, error, message, Instant.now());
    }
}
