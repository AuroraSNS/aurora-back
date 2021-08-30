package com.center.aurora.utils;

import com.center.aurora.domain.user.AuthProvider;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.Auth.Dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Validators {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Message validateSignUp(String email, String username, String password, String passwordConfirm){
        HttpStatus statusCode = null;
        String message = "";

        if (username==null || email==null || password==null || passwordConfirm==null) {
            statusCode = HttpStatus.BAD_REQUEST; //400
            message = "모든 항목을 채워주세요";
        }
        else if (!password.equals(passwordConfirm)) {
            statusCode = HttpStatus.CONFLICT; //409
            message = "비밀번호가 서로 다릅니다";
        }
        else if (!validateEmail(email)) {
            statusCode = HttpStatus.CONFLICT; //409
            message = "이메일 형식이 올바르지 않습니다";
        }
        else if (!validatePassword(password)) {
            statusCode = HttpStatus.CONFLICT; //409
            message = "잘못된 비밀번호 입니다";
        }
        else if (!validateUserName(username)) {
            statusCode = HttpStatus.CONFLICT; //409
            message = "유저네임은 12자 이하이어야 합니다";
        }

        else if (userRepository.existsByEmail(email)) {
            statusCode = HttpStatus.CONFLICT; //409
            message = "이미 가입된 이메일입니다";
        }

        return Message.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    public Message validateSignInUser(String email, String password){
        HttpStatus statusCode = null;
        String message = "";

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (email==null || password==null) {
            statusCode = HttpStatus.BAD_REQUEST; //400
            message = "모든 항목을 채워주세요";
        }

        else if(optionalUser.isEmpty()){
            statusCode = HttpStatus.UNAUTHORIZED; //401
            message = "가입하지 않은 이메일입니다";
        }

        else if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            statusCode = HttpStatus.UNAUTHORIZED; //401
            message = "잘못된 비밀번호 입니다";
        }

        else if (optionalUser.get().getProvider() != AuthProvider.local) {
            statusCode = HttpStatus.UNAUTHORIZED; //401
            message = "로그인 방식이 다릅니다";
        }

        return Message.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    public boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$", email);
    }

    public boolean validatePassword(String password){
        return Pattern.matches("^[a-zA-z0-9!@#$%^&*]{4,12}$", password);
    }

    public boolean validateUserName(String username){
        return (username.length() >= 1 && username.length() <= 12);
    }
}