package com.company.bugtracker1.sla.exception;

public class SLAPolicyException extends RuntimeException {
    public SLAPolicyException(String message) {
        super(message);
    }

    public SLAPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}
