package com.center.aurora.service.comment;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.comment.dto.CommentDto;
import com.center.aurora.service.comment.dto.CommentResponse;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("댓글 생성")
    @Test
    void createComment() throws IOException {
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
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();

        //when
        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);

        //then
        List<CommentResponse> result = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");

        assertThat(result.get(0).getContent()).isEqualTo("comment1");
    }

    @DisplayName("댓글 조회")
    @Test
    void getComment() throws IOException {
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

        postService.createPost(userA.getId(), postDto);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "id");
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();
        CommentDto commentDto2 = CommentDto.builder().content("comment2").build();

        //when
        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        commentService.createComment(userB.getId(),posts.get(0).getId(), commentDto2);

        //then
        List<CommentResponse> result = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");

        assertThat(result.get(1).getContent()).isEqualTo("comment1");
        assertThat(result.get(1).getAuth().getId()).isEqualTo(userA.getId());

        assertThat(result.get(0).getContent()).isEqualTo("comment2");
        assertThat(result.get(0).getAuth().getId()).isEqualTo(userB.getId());
    }

    @DisplayName("댓글 수정")
    @Test
    void updateComment() throws IOException {
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
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();
        CommentDto commentDto2 = CommentDto.builder().content("comment2").build();

        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        List<CommentResponse> comments = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");

        assertThat(comments.get(0).getContent()).isEqualTo("comment1");

        //when
        commentService.updateComment(userA.getId(),comments.get(0).getId(),commentDto2);

        //then
        List<CommentResponse> result = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");
        assertThat(result.get(0).getContent()).isEqualTo("comment2");
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() throws IOException {
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
        List<PostResponse> posts = postService.getPost(userA.getId(),pageable);

        CommentDto commentDto = CommentDto.builder().content("comment1").build();

        commentService.createComment(userA.getId(),posts.get(0).getId(), commentDto);
        List<CommentResponse> comments = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");

        assertThat(comments.get(0).getContent()).isEqualTo("comment1");

        //when
        commentService.deleteComment(userA.getId(),comments.get(0).getId());

        //then
        List<CommentResponse> result = (List<CommentResponse>) commentService.getComment(posts.get(0).getId()).get("comments");
        assertThat(result.size()).isEqualTo(0);
    }


}
