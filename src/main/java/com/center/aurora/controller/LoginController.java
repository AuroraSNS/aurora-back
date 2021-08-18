package com.center.aurora.controller;

import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    /** TODO
     *  OAuth2말고 커스텀 로그인 하기
     *  로그인 완료후 토큰 넣기
     * */
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = tokenProvider.createToken(authentication);
//        Cookie cookie = new Cookie("Authorization", token);
//        response.addCookie(cookie);
//
//
//        log.info("LOGIN");
//        return ResponseEntity.ok(new AuthResponse(token));
//    }

    /** TODO
     *  커스텀 회원 가입
     * */
//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new BadRequestException("이미 사용중인 이메일입니다.");
//        }
//
//        // 계정 생성
//        // TODO 계정 생성 부분 분리 하기
//        User result = userRepository.save(User.builder()
//                .name(signUpRequest.getName())
//                .email(signUpRequest.getEmail())
//                .provider(AuthProvider.local)
//                .build()
//        );
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/user/me")
//                .buildAndExpand(result.getId()).toUri();
//
//        return ResponseEntity.created(location)
//                .body(new ApiResponse(true, "성공적으로 계정 생성이 되었습니다."));
//    }

}