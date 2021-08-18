package com.center.aurora.service.post.dto;

import com.center.aurora.domain.post.Mood;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostCreateRequestDto {

    Mood mood;
    String content;
    List<MultipartFile> images;

    @Builder
    public PostCreateRequestDto(Mood mood, String content, List<MultipartFile> images) {
        this.mood = mood;
        this.content = content;
        this.images = images;
    }
}
