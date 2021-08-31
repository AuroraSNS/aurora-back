package com.center.aurora.service.post.dto;

import com.center.aurora.service.post.dto.PostUserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {
    private Long id;
    private PostUserDto auth;
    private String content;

    @Builder
    public CommentResponse(Long id, PostUserDto auth, String content) {
        this.id = id;
        this.auth = auth;
        this.content = content;
    }
}
