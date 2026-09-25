package com.cis.metering_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private String message;
    private String pathname;
    private HttpStatus status;
}
