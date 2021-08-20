package com.center.aurora.controller;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.post.ImageRepository;
import com.center.aurora.repository.post.PostRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostCreateRequestDto;
import com.center.aurora.service.post.dto.PostResponse;
import com.center.aurora.utils.CookieUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenProvider tokenProvider;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();
        imageRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("게시물 생성")
    @Test
    public void createPost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        String url = "http://localhost:" + port + "/posts";
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        //when
        mvc.perform(post(url)
                        .param("content", "content1")
                        .param("mood", "sun")
                        .cookie(accessToken))
                .andExpect(status().isOk());

        //then
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getAllPost(pageable);

        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);
    }

    @DisplayName("모든 게시물 조회")
    @Test
    void getAllPost() throws Exception {
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostCreateRequestDto postDto = PostCreateRequestDto.builder().mood(Mood.sun).content("content1").build();
        PostCreateRequestDto postDto2 = PostCreateRequestDto.builder().mood(Mood.moon).content("content2").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);

        //when
        String url = "http://localhost:" + port + "/posts/all?page=0";
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        //then
        mvc.perform(get(url).cookie(accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("특정 유저 게시물 조회")
    @Test
    void getPost() throws Exception {
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostCreateRequestDto postDto = PostCreateRequestDto.builder().mood(Mood.sun).content("content1").build();
        PostCreateRequestDto postDto2 = PostCreateRequestDto.builder().mood(Mood.moon).content("content2").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);

        //when
        String url = "http://localhost:" + port + "/posts/" + userA.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        //then
        mvc.perform(get(url).cookie(accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("게시물 수정")
    @Test
    void updatePost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostCreateRequestDto postDto = PostCreateRequestDto.builder().mood(Mood.sun).content("content1").build();

        PostResponse post = postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        //when
        String url = "http://localhost:" + port + "/posts/" + post.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        mvc.perform(patch(url)
                        .param("content", "content2")
                        .param("mood", "moon")
                        .cookie(accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getContent()).isEqualTo("content2");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.moon);
    }

    @DisplayName("게시물 삭제")
    @Test
    void deletePost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostCreateRequestDto postDto = PostCreateRequestDto.builder().mood(Mood.sun).content("content1").build();

        PostResponse post = postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        //when
        String url = "http://localhost:" + port + "/posts/" + post.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        mvc.perform(delete(url)
                        .cookie(accessToken))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        result = postService.getPost(userA.getId(),pageable);

        assertThat(result.size()).isEqualTo(0);
    }
}
