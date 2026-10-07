package com.cis.billing_service.exception;

public class BillAlreadyExistsException extends RuntimeException {
    public BillAlreadyExistsException(String s) {
        super(s);
    }
}
