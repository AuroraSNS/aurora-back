package com.center.aurora.controller.auth;

import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.auth.AuthService;
import com.center.aurora.service.auth.dto.AuthDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입")
    @Test
    public void signUp() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(map)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("회원 가입이 성공적으로 완료되었습니다!");
    }

    @DisplayName("이메일 유효성 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void ValidateEmail() throws Exception {
        //given
        String email = "aa.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("이메일 형식이 올바르지 않습니다");
    }

    @DisplayName("비밀 번호 유효성 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void ValidatePassword() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "123";
        String passwordConfirm = "123";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("잘못된 비밀번호 입니다");
    }

    @DisplayName("비밀 번호 일치 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void ValidatePasswordConfirm() throws Exception {
        //given
        String email = "aa.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "12345";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("비밀번호가 서로 다릅니다");
    }

    @DisplayName("이메일 중복 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void DuplicateEmail() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        authService.signUp(authDto);
        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("이미 가입된 이메일입니다");
    }

    @DisplayName("이름 유효성 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void ValidateUserName() throws Exception {
        //given
        String email = "a@a.com";
        String name = "AAAABBBBCCCCD";
        String password = "1234";
        String passwordConfirm = "1234";

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("name", name);
        map.put("password", password);
        map.put("passwordConfirm", passwordConfirm);

        String url = "http://localhost:" + port + "/signup";

        //when
        MvcResult res = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        String result = res.getResponse().getContentAsString();

        //then
        assertThat(result).isEqualTo("유저네임은 12자 이하이어야 합니다");
    }

    @DisplayName("로그인") //이메일 형식에 따라야 합니다.
    @Test
    public void signIn() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", password);

        //when
        String url = "http://localhost:" + port + "/signin";

        //then
        mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(map)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("로그인 이메일 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void existsEmail() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", "b@b.com");
        map.put("password", password);

        //when
        String url = "http://localhost:" + port + "/signin";

        //then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("로그인 비밀번호 검사") //이메일 형식에 따라야 합니다.
    @Test
    public void signInPasswordConfirm() throws Exception {
        //given
        String email = "a@a.com";
        String name = "A";
        String password = "1234";
        String passwordConfirm = "1234";

        AuthDto authDto = AuthDto.builder().email(email).name(name).password(password).passwordConfirm(passwordConfirm).build();
        authService.signUp(authDto);

        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", email);
        map.put("password", "12345");

        //when
        String url = "http://localhost:" + port + "/signin";

        //then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}