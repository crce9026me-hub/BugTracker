package com.company.bugtracker1.sla.exception;

public class SLAException extends RuntimeException {
    public SLAException(String message) {
        super(message);
    }

    public SLAException(String message, Throwable cause) {
        super(message, cause);
    }
}
