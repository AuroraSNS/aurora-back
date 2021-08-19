package com.center.aurora.exception;

public class NoCookieException extends RuntimeException{
    public NoCookieException(String message) {
        super(message);
    }

    public NoCookieException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCookieException(Throwable cause) {
        super(cause);
    }

    public NoCookieException() {
    }
}
