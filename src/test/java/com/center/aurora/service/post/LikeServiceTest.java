package com.center.aurora.service.post;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.post.LikeService;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostDto;
import com.center.aurora.service.post.dto.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LikeServiceTest {
    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("좋아요 추가")
    @Test
    void createLike() throws IOException {
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(0);

        //when
        likeService.createLike(userA.getId(), posts.get(0).getId());

        //then
        posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(1);
    }

    @DisplayName("좋아요 삭제")
    @Test
    void deleteLike() throws IOException {
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

        //when
        likeService.deleteLike(userA.getId(), posts.get(0).getId());

        //then
        posts = postService.getPost(userA.getId(),pageable);
        assertThat(posts.get(0).getLikeCnt()).isEqualTo(0);
    }
}
