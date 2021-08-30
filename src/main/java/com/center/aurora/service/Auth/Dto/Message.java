package com.center.aurora.service.Auth.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class Message {
    private HttpStatus statusCode;
    private String message;

    @Builder
    public Message(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}