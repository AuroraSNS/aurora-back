package com.center.aurora.controller;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.post.dto.PostCreateRequestDto;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/all")
    public List<PostResponse> getAllPosts(@PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPost(pageable);
    }

    @GetMapping("/posts/{userId}")
    public List<PostResponse> getPosts(@PathVariable("userId") Long user_id, @PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPost(user_id, pageable);
    }

    @PostMapping("/posts")
    public PostResponse createPost(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute PostCreateRequestDto postDto) throws IOException {
        return postService.createPost(userPrincipal.getId(),postDto);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse updatePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id, @ModelAttribute PostCreateRequestDto postDto) throws IOException {
        return postService.updatePost(userPrincipal.getId(), post_id, postDto);
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id) throws IOException {
        return postService.deletePost(userPrincipal.getId(), post_id);
    }
}