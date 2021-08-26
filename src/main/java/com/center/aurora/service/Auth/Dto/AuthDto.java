package com.center.aurora.service.Auth.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String password;
    private String name;
}