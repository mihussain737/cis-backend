package com.cis.billing_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String pathname;
    private LocalDateTime timestamp;
    private HttpStatus status;
}
