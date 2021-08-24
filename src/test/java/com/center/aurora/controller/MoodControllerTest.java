package com.center.aurora.controller;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.mood.MoodService;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoodControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MoodService moodService;

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

    @DisplayName("모든 게시물 날씨 통계")
    @Test
    public void getAllMood() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        PostDto postDto2 = PostDto.builder().mood(Mood.cloud).content("content2").build();
        PostDto postDto3 = PostDto.builder().mood(Mood.rain).content("content3").build();
        PostDto postDto4 = PostDto.builder().mood(Mood.moon).content("content4").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userA.getId(), postDto2);
        postService.createPost(userA.getId(), postDto3);
        postService.createPost(userA.getId(), postDto4);

        //when
        String url = "http://localhost:" + port + "/mood/all";

        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("특정 유저 게시물 날씨 통계")
    @Test
    public void getMoodByUser() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        PostDto postDto2 = PostDto.builder().mood(Mood.cloud).content("content2").build();
        PostDto postDto3 = PostDto.builder().mood(Mood.rain).content("content3").build();
        PostDto postDto4 = PostDto.builder().mood(Mood.moon).content("content4").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userA.getId(), postDto2);
        postService.createPost(userB.getId(), postDto3);
        postService.createPost(userB.getId(), postDto4);

        //when
        String url = "http://localhost:" + port + "/mood/" + userA.getId();

        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
