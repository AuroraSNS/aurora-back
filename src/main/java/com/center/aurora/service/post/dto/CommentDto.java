package com.center.aurora.service.post.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class CommentDto {
    private String content;

    @Builder
    public CommentDto(String content){
        this.content = content;
    }
}
