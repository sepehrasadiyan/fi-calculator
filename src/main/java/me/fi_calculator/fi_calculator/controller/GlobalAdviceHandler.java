package me.fi_calculator.fi_calculator.controller;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import me.fi_calculator.fi_calculator.domain.generic.ApiResponse;
import me.fi_calculator.fi_calculator.domain.generic.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalAdviceHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        var body = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidation(MethodArgumentNotValidException ex) {
        BindingResult br = ex.getBindingResult();

        List<ValidationError> errors = br.getFieldErrors().stream()
                .map(this::toValidationError)
                .collect(Collectors.toList());

        errors.addAll(br.getGlobalErrors().stream()
                .map(this::toValidationError)
                .toList());

        Map<String, Object> payload = Map.of(
                "errors", errors,
                "errorCount", errors.size()
        );

        var body = ApiResponse.error(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation Error",
                "One or more fields are invalid"
        );
        var response = new ApiResponse<>(false, body.status(), payload, body.error(),
                body.message(), body.timestamp());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    private ValidationError toValidationError(FieldError fe) {
        return new ValidationError(
                fe.getField(),
                fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value",
                "password".equalsIgnoreCase(fe.getField()) ? null : fe.getRejectedValue(),
                fe.getCode()
        );
    }

    private ValidationError toValidationError(ObjectError oe) {
        return new ValidationError(
                oe.getObjectName(),
                oe.getDefaultMessage() != null ? oe.getDefaultMessage() : "Invalid request",
                null,
                oe.getCode()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ValidationError> errors = ex.getConstraintViolations().stream()
                .map(this::toValidationError)
                .toList();

        Map<String, Object> payload = Map.of(
                "errors", errors,
                "errorCount", errors.size()
        );

        var body = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "Constraint Violation",
                "Invalid parameter(s)"
        );
        var response = new ApiResponse<>(false, body.status(),
                payload, body.error(), body.message(), body.timestamp());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ValidationError toValidationError(ConstraintViolation<?> cv) {
        String field = Optional.ofNullable(cv.getPropertyPath()).map(Object::toString).orElse("unknown");
        return new ValidationError(
                field,
                cv.getMessage(),
                // I don’t leak passwords :)
                field.toLowerCase().contains("password") ? null : cv.getInvalidValue(),
                cv.getConstraintDescriptor() != null ?
                        cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName() : null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        var body = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error", "Unexpected error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
