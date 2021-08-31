package com.center.aurora.service.auth;

import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.auth.dto.AuthDto;
import com.center.aurora.service.auth.dto.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입")
    @Test
    void signUp() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        authService.signUp(authDto);

        //then
        User user = userRepository.findByEmail(authDto.getEmail()).get();
        assertThat(user.getName()).isEqualTo("A");
    }

    @DisplayName("이메일 유효성 검사") //이메일 형식에 따라야 합니다.
    @Test
    void ValidateEmail() {
        //given
        String email = "aa.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        ResponseEntity<String> result = authService.signUp(authDto);

        //then
        assertThat(result.getBody()).isEqualTo("이메일 형식이 올바르지 않습니다");
    }

    @DisplayName("비밀 번호 유효성 검사") // 비밀번호는 4자 이상 12자 이하 입니다.
    @Test
    void ValidatePassword() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "123";
        String passwordConfirm = "123";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        ResponseEntity<String> result = authService.signUp(authDto);

        //then
        assertThat(result.getBody()).isEqualTo("잘못된 비밀번호 입니다");
    }

    @DisplayName("비밀 번호 일치 검사")
    @Test
    void ValidatePasswordConfirm() {
        //given
        String email = "aa.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "12345";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        ResponseEntity<String> result = authService.signUp(authDto);

        //then
        assertThat(result.getBody()).isEqualTo("비밀번호가 서로 다릅니다");
    }

    @DisplayName("이메일 중복 검사")
    @Test
    void DuplicateEmail() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        authService.signUp(authDto);
        ResponseEntity<String> result = authService.signUp(authDto);

        //then
        assertThat(result.getBody()).isEqualTo("이미 가입된 이메일입니다");
    }

    @DisplayName("이름 유효성 검사")
    @Test
    void ValidateUserName() {
        //given
        String email = "a@a.com";
        String name = "AAAABBBBCCCCD";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        //when
        ResponseEntity<String> result = authService.signUp(authDto);

        //then
        assertThat(result.getBody()).isEqualTo("유저네임은 12자 이하이어야 합니다");
    }

    @DisplayName("로그인")
    @Test
    void signIn() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        //when

        ResponseEntity<SignInResponse> result = authService.signIn(authDto);

        //then
        assertThat(result.getBody().getMessage()).isEqualTo("로그인 성공했습니다");
    }

    @DisplayName("로그인 이메일 검사")
    @Test
    void existsEmail() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        //when
        authDto = AuthDto.builder().email("b@b.com").password(password).build();

        ResponseEntity<SignInResponse> result = authService.signIn(authDto);

        //then
        assertThat(result.getBody().getMessage()).isEqualTo("가입하지 않은 이메일입니다");
    }

    @DisplayName("로그인 비밀번호 검사")
    @Test
    void signInPasswordConfirm() {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        //when
        authDto = AuthDto.builder().email(email).password("12345").build();

        ResponseEntity<SignInResponse> result = authService.signIn(authDto);

        //then
        assertThat(result.getBody().getMessage()).isEqualTo("잘못된 비밀번호 입니다");
    }
}