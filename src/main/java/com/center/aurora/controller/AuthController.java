package com.center.aurora.controller;

import com.center.aurora.service.Auth.AuthService;
import com.center.aurora.service.Auth.Dto.AuthDto;
import com.center.aurora.service.Auth.Dto.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthDto authDto){
        return authService.signUp(authDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody AuthDto authDto){
        return authService.signIn(authDto);
    }
}