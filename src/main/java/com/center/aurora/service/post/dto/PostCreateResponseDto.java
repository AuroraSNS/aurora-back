package com.center.aurora.service.post.dto;

import com.center.aurora.domain.post.Mood;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostCreateResponseDto {

    Long writer;
    Mood mood;
    String content;
    List<String> image;

    @Builder
    public PostCreateResponseDto(Long writer, Mood mood, String content, List<String> image) {
        this.writer = writer;
        this.mood = mood;
        this.content = content;
        this.image = image;
    }
}
