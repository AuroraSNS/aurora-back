package com.center.aurora.service.post.dto;

import com.center.aurora.domain.post.Mood;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponse {

    Long id;
    PostListUserDto auth;
    Mood mood;
    String content;
    List<String> images;

    @Builder
    public PostListResponse(Long id, PostListUserDto getAllPostUser, Mood mood, String content, List<String> images) {
        this.id = id;
        this.auth = getAllPostUser;
        this.mood = mood;
        this.content = content;
        this.images = images;
    }
}