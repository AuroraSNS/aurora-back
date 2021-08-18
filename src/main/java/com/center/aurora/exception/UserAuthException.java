package com.center.aurora.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAuthException extends AuthenticationException {
    public UserAuthException(String msg) {
        super(msg);
    }
}
