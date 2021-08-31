package com.center.aurora.service.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponse {
    private String token;
    private String message;

    @Builder
    public SignInResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}