package com.center.aurora.service.Auth.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String name;
    private String password;
    private String passwordConfirm;

    @Builder
    public AuthDto(String email, String name, String password, String passwordConfirm) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
}