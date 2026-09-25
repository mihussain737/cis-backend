package com.cis.metering_service.exception;

public class ConsumerNotFoundException extends RuntimeException{
    public ConsumerNotFoundException() {
    }
    public ConsumerNotFoundException(String message) {
        super(message);
    }
}
