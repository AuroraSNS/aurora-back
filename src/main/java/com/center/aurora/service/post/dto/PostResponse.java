package com.center.aurora.service.post.dto;

import com.center.aurora.domain.post.Mood;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponse {

    private Long id;
    private PostUserDto auth;
    private Mood mood;
    private String content;
    private List<String> images;

    @Builder
    public PostResponse(Long id, PostUserDto getAllPostUser, Mood mood, String content, List<String> images) {
        this.id = id;
        this.auth = getAllPostUser;
        this.mood = mood;
        this.content = content;
        this.images = images;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", auth=" + auth +
                ", mood=" + mood +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }
}