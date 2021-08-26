package com.center.aurora.controller;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.like.LikeService;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostDto;
import com.center.aurora.service.post.dto.PostResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

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
        userRepository.deleteAll();
    }

    @DisplayName("좋아요 추가")
    @Test
    public void createLike() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(0);

        String url = "http://localhost:" + port + "/likes/" + posts.get(0).getId();
        String token = tokenProvider.createTokenByUserEntity(userA);

        //when
        mvc.perform(post(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(1);
    }

    @DisplayName("좋아요 삭제")
    @Test
    public void deleteLike() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        likeService.createLike(userA.getId(), posts.get(0).getId());
        posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(1);

        String url = "http://localhost:" + port + "/likes/" + posts.get(0).getId();
        String token = tokenProvider.createTokenByUserEntity(userA);

        //when
        mvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(0);
    }
}
