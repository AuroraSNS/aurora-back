package com.center.aurora.service.auth;

import com.center.aurora.domain.user.AuthProvider;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.auth.dto.AuthDto;
import com.center.aurora.service.auth.dto.Message;
import com.center.aurora.service.auth.dto.SignInResponse;
import com.center.aurora.utils.Validators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final Validators validators;

    @Transactional
    public ResponseEntity<String> signUp(AuthDto authDto) {
        Message validate = validators.validateSignUp(authDto.getEmail(), authDto.getName(), authDto.getPassword(), authDto.getPasswordConfirm());

        if (validate.getStatusCode()!=null) {
            return new ResponseEntity<>(validate.getMessage(),validate.getStatusCode());
        }

        userRepository.save(User.builder()
                .name(authDto.getName())
                .email(authDto.getEmail())
                .image(User.DEFAULT_IMAGE_URL)
                .role(Role.USER)
                .provider(AuthProvider.local)
                .password(passwordEncoder.encode(authDto.getPassword()))
                .build()
        );

        return new ResponseEntity<>("회원 가입이 성공적으로 완료되었습니다!", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<SignInResponse> signIn(AuthDto authDto) {
        Message validate = validators.validateSignInUser(authDto.getEmail(), authDto.getPassword());
        SignInResponse signupResponse;

        if (validate.getStatusCode()!=null) {
            signupResponse = SignInResponse.builder()
                    .message(validate.getMessage())
                    .token(null)
                    .build();
            return new ResponseEntity<>(signupResponse,validate.getStatusCode());
        }

        User user = userRepository.findByEmail(authDto.getEmail()).get();
        signupResponse = SignInResponse.builder()
                .message("로그인 성공했습니다")
                .token(tokenProvider.createTokenByUserEntity(user))
                .build();

        return new ResponseEntity<>(signupResponse, HttpStatus.OK);
    }
}
