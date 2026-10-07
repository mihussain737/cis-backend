package com.cis.billing_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BillAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBillAlreadyExistsException(BillAlreadyExistsException ex, WebRequest webRequest){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(org.springframework.http.HttpStatus.CONFLICT);
        errorResponse.setTimestamp(java.time.LocalDateTime.now());
        errorResponse.setPathname(webRequest.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest webRequest){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(org.springframework.http.HttpStatus.CONFLICT);
        errorResponse.setTimestamp(java.time.LocalDateTime.now());
        errorResponse.setPathname(webRequest.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}
}
