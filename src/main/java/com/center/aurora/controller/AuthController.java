package com.center.aurora.controller;

import com.center.aurora.service.Auth.AuthService;
import com.center.aurora.service.Auth.Dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody AuthDto authDto){
        Long userId = authService.signUp(authDto);
        return ResponseEntity.created(URI.create("/user/" + userId)).build();
    }

    @PostMapping("/signin")
    public Map signIn(@RequestBody AuthDto authDto){
        return authService.signIn(authDto);
    }
}