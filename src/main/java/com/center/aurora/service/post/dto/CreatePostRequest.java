package com.center.aurora.service.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class CreatePostRequest {

    Long writer;
    String mood;
    String content;
    List<String> image;

    @Builder
    public CreatePostRequest(Long writer, String mood, String content, List<String> image) {
        this.writer = writer;
        this.mood = mood;
        this.content = content;
        this.image = image;
    }
}