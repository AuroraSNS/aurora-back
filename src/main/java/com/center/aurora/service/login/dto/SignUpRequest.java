package com.center.aurora.service.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private String name;
    private String email;

    @Builder
    public SignUpRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
