package com.center.aurora.controller;


import com.center.aurora.service.post.dto.CreatePostRequest;
import com.center.aurora.service.post.dto.CreatePostResponse;
import com.center.aurora.service.post.CreatePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CreatePostController {
    private final CreatePostService postService;

    @PostMapping("/post")
    public CreatePostRequest createPost(@ModelAttribute CreatePostResponse postDto) throws IOException {
        return postService.createPost(postDto);
    }
}
