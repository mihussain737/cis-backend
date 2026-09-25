package com.cis.metering_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseException(
            HttpMessageNotReadableException exception
    ) {

        Throwable cause = exception;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        log.error("JSON parsing failed");
        log.error("Root cause: {}", cause.getMessage(), exception);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("message", "Invalid JSON request");
        response.put("details", cause.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(ConsumerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleJsonParseException(ConsumerNotFoundException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(exception.getMessage());
        exceptionResponse.setTimestamp(LocalDateTime.now());
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND);
        exceptionResponse.setPathname(request.getContextPath());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}