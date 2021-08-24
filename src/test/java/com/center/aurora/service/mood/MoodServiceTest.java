package com.center.aurora.service.mood;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.mood.dto.MoodResponse;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MoodServiceTest {
    @Autowired
    private MoodService moodService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("모든 게시물 날씨 통계")
    @Test
    void getAllMood() throws IOException {
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
        MoodResponse result = moodService.getAllMood();

        //then
        assertThat(result.getSun()).isEqualTo(25);
        assertThat(result.getCloud()).isEqualTo(25);
        assertThat(result.getRain()).isEqualTo(25);
        assertThat(result.getMoon()).isEqualTo(25);
    }

    @DisplayName("특정 유저 게시물 날씨 통계")
    @Test
    void getMoodByUser() throws IOException {
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
        MoodResponse result = moodService.getMoodByUser(userA.getId());

        //then
        assertThat(result.getSun()).isEqualTo(50);
        assertThat(result.getCloud()).isEqualTo(50);
        assertThat(result.getRain()).isEqualTo(0);
        assertThat(result.getMoon()).isEqualTo(0);
    }
}
