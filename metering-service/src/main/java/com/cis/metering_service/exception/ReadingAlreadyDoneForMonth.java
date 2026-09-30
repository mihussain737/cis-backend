package com.cis.metering_service.exception;

public class ReadingAlreadyDoneForMonth extends RuntimeException{
    public ReadingAlreadyDoneForMonth(String message){
        super(message);
    }
}
