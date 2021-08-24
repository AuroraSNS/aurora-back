package com.center.aurora.controller;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.comment.CommentService;
import com.center.aurora.service.comment.dto.CommentDto;
import com.center.aurora.service.comment.dto.CommentResponse;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostDto;
import com.center.aurora.service.post.dto.PostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
public class CommentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CommentService commentService;

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

    @DisplayName("댓글 생성")
    @Test
    public void createPost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        String url = "http://localhost:" + port + "/comments/" + posts.get(0).getId();
        String token = tokenProvider.createTokenByUserEntity(userA);

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString("comment1"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<CommentResponse> result = commentService.getComment(posts.get(0).getId());

        assertThat(result.get(0).getContent()).isEqualTo("comment1");
    }

    @DisplayName("댓글 조회")
    @Test
    void getAllPost() throws Exception {
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();
        CommentDto commentDto2 = CommentDto.builder().content("comment2").build();

        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        commentService.createComment(userB.getId(),posts.get(0).getId(), commentDto2);

        //when
        String url = "http://localhost:" + port + "/comments/" + posts.get(0).getId();

        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("댓글 수정")
    @Test
    void updatePost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();

        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        List<CommentResponse> comments = commentService.getComment(posts.get(0).getId());

        assertThat(comments.get(0).getContent()).isEqualTo("comment1");

        //when
        String url = "http://localhost:" + port + "/comments/" + comments.get(0).getId();
        String token = tokenProvider.createTokenByUserEntity(userA);

        mvc.perform(patch(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString("comment2"))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<CommentResponse> result = commentService.getComment(posts.get(0).getId());

        assertThat(result.get(0).getContent()).isEqualTo("comment2");
    }

    @DisplayName("게시물 삭제")
    @Test
    void deletePost() throws Exception{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();

        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        List<CommentResponse> comments = commentService.getComment(posts.get(0).getId());

        assertThat(comments.get(0).getContent()).isEqualTo("comment1");

        //when
        String url = "http://localhost:" + port + "/comments/" + comments.get(0).getId();
        String token = tokenProvider.createTokenByUserEntity(userA);

        mvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        List<CommentResponse> result = commentService.getComment(posts.get(0).getId());
        assertThat(result.size()).isEqualTo(0);
    }
}
