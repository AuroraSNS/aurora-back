package com.center.aurora.service.post;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("게시물 생성")
    @Test
    void createPost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();

        //when
        postService.createPost(userA.getId(), postDto);

        //then
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getAllPost(pageable);

        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);
    }


    @DisplayName("모든 게시물 조회")
    @Test
    void getAllPost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();
        PostDto postDto2 = PostDto.builder().mood(Mood.moon).content("content2").images(imageList).build();

        //when
        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);

        //then
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getAllPost(pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userB.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content2");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.moon);

        assertThat(result.get(1).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(1).getContent()).isEqualTo("content1");
        assertThat(result.get(1).getMood()).isEqualTo(Mood.sun);

        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("특정 유저 게시물 조회")
    @Test
    void getPost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();
        PostDto postDto2 = PostDto.builder().mood(Mood.moon).content("content2").images(imageList).build();

        //when
        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);

        //then
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("특정 게시물 조회")
    @Test
    void getOnePost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();
        PostDto postDto2 = PostDto.builder().mood(Mood.moon).content("content2").images(imageList).build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        //when
        PostResponse result = postService.getOnePost(posts.get(0).getId());

        //then
        assertThat(result.getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.getContent()).isEqualTo("content1");
        assertThat(result.getMood()).isEqualTo(Mood.sun);
    }

    @DisplayName("날씨 별 게시물 조회")
    @Test
    void getAllPostByMood() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        PostDto postDto2 = PostDto.builder().mood(Mood.sun).content("content2").build();
        PostDto postDto3 = PostDto.builder().mood(Mood.rain).content("content3").build();
        PostDto postDto4 = PostDto.builder().mood(Mood.moon).content("content4").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);
        postService.createPost(userA.getId(), postDto3);
        postService.createPost(userB.getId(), postDto4);

        List<Mood> moodList = new ArrayList<>();
        moodList.add(Mood.sun);

        //when
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getAllPostByMood(pageable,moodList);

        //then
        assertThat(result.get(0).getAuth().getId()).isEqualTo(userB.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content2");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        assertThat(result.get(1).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(1).getContent()).isEqualTo("content1");
        assertThat(result.get(1).getMood()).isEqualTo(Mood.sun);

        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("특정 유저 및 날씨 별 게시물 조회")
    @Test
    void getPostByUserAndMood() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);
        userRepository.save(userB);

        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").build();
        PostDto postDto2 = PostDto.builder().mood(Mood.sun).content("content2").build();
        PostDto postDto3 = PostDto.builder().mood(Mood.rain).content("content3").build();
        PostDto postDto4 = PostDto.builder().mood(Mood.moon).content("content4").build();

        postService.createPost(userA.getId(), postDto);
        postService.createPost(userB.getId(), postDto2);
        postService.createPost(userA.getId(), postDto3);
        postService.createPost(userB.getId(), postDto4);

        List<Mood> moodList = new ArrayList<>();
        moodList.add(Mood.sun);

        //when
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPostByUserAndMood(userA.getId(), pageable, moodList);

        //then
        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("게시물 수정")
    @Test
    void updatePost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);

        PostDto postDto2 = PostDto.builder().mood(Mood.moon).content("content2").images(imageList).build();

        //when
        postService.updatePost(userA.getId(), result.get(0).getId(), postDto2);

        //then
        result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getContent()).isEqualTo("content2");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.moon);
    }

    @DisplayName("게시물 삭제")
    @Test
    void deletePost() throws IOException{
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        userRepository.save(userA);

        MultipartFile images = new MockMultipartFile(
                "test.png",
                "origin.png",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream("src/test/resources/images/image.jpg")
        );
        List<MultipartFile> imageList = new ArrayList<>();
        imageList.add(images);
        PostDto postDto = PostDto.builder().mood(Mood.sun).content("content1").images(imageList).build();

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> result = postService.getPost(userA.getId(),pageable);

        assertThat(result.get(0).getAuth().getId()).isEqualTo(userA.getId());
        assertThat(result.get(0).getContent()).isEqualTo("content1");
        assertThat(result.get(0).getMood()).isEqualTo(Mood.sun);
        assertThat(result.size()).isEqualTo(1);

        //when
        postService.deletePost(userA.getId(), result.get(0).getId());

        //then
        result = postService.getPost(userA.getId(),pageable);
        assertThat(result.size()).isEqualTo(0);
    }
}
