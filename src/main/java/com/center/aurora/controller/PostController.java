package com.center.aurora.controller;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.post.dto.PostCreateResponseDto;
import com.center.aurora.service.post.dto.PostCreateRequestDto;
import com.center.aurora.service.post.PostService;
import com.center.aurora.service.post.dto.PostListResponse;
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
    public List<PostListResponse> getAllPosts(@PageableDefault(size=10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllPost(pageable);
    }

    @PostMapping("/posts")
    public PostCreateResponseDto createPost(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute PostCreateRequestDto postDto) throws IOException {
        return postService.createPost(userPrincipal.getId(),postDto);
    }
}