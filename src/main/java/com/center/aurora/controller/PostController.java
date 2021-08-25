package com.center.aurora.controller;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.post.dto.PostDto;
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

    @GetMapping("/posts/all/filter")
    public List<PostResponse> getAllPostByMood(@RequestParam List<Mood> mood, @PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        return postService.getAllPostByMood(pageable, mood);
    }

    @GetMapping("/posts/{userId}/filter")
    public List<PostResponse> getPostByUserAndMood(@PathVariable("userId") Long user_id, @RequestParam List<Mood> mood, @PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
        return postService.getPostByUserAndMood(user_id, pageable, mood);
    }

    @GetMapping("/posts/all")
    public List<PostResponse> getAllPosts(@PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPost(pageable);
    }

    @GetMapping("/posts/{userId}")
    public List<PostResponse> getPosts(@PathVariable("userId") Long user_id, @PageableDefault(size=5, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPost(user_id, pageable);
    }

    @PostMapping("/posts")
    public void createPost(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute PostDto postDto) throws IOException {
        postService.createPost(userPrincipal.getId(),postDto);
    }

    @PatchMapping("/posts/{postId}")
    public void updatePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id, @ModelAttribute PostDto postDto) throws IOException {
        postService.updatePost(userPrincipal.getId(), post_id, postDto);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id) throws IOException {
        postService.deletePost(userPrincipal.getId(), post_id);
    }
}