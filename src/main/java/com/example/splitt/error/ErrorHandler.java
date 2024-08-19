package com.example.splitt.error;

import com.example.splitt.error.exception.EntityNotFoundException;
import com.example.splitt.error.model.ApiError;
import com.example.splitt.error.exception.CustomValidationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final String SPLITT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        log.warn("400 - Bad Request: {}", details);

        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                String.join(",", details),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        log.warn("400 - Bad Request: {}", exception.getMessage());
        String errorMessage = "Request Body Missing";

        Throwable cause = exception.getCause();
        if (cause instanceof InvalidFormatException) {
            errorMessage = exception.getMessage();
        }

        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                errorMessage,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingRequestHeaderException(final MissingRequestHeaderException exception) {
        log.warn("400 - Bad Request: {}", exception.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                exception.getBody().getDetail(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        log.warn("400 - Bad Request: {}", exception.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                String.format("Incorrect input for parameter '%s'. Received input: %s",
                        exception.getName(), exception.getValue()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingPathVariableException(final MissingPathVariableException exception) {
        log.warn("400 - Bad Request: {}", exception.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                "Missing path variable.",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleCustomValidationException(final CustomValidationException exception) {
        log.warn("400 - Bad Request: {} {}", exception.getReason(), exception.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST,
                exception.getReason(),
                exception.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        log.warn("404 - Not Found: {} - {}", exception.getMessage(), exception.getEntityClass());
        return new ApiError(HttpStatus.NOT_FOUND,
                "Entity not found.",
                exception.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @SneakyThrows
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        ApiError apiError = buildApiError(exception);
        log.warn("409 - Conflict: {}", apiError.getMessage());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalException(final Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String errorMessage = stringWriter.toString();

        log.warn("500 - Internal Server Error: {}", errorMessage);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "500 - Internal Server Error",
                errorMessage,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT))
        );
    }

    // ------------------
    // Private methods
    // ------------------

    private ApiError buildApiError(final DataIntegrityViolationException exception) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(SPLITT_DATE_TIME_FORMAT)));

        // Duplicate Email
        if (exception.getMessage().contains("users_email_unique")) {
            apiError.setReason("Duplicate Email");
            apiError.setMessage("Duplicate Email. A user with this email already exists");
            return apiError;
        }

        // Generic
        apiError.setReason("Integrity Constraint Violated.");
        apiError.setMessage(exception.getMessage());
        return apiError;
    }
}
