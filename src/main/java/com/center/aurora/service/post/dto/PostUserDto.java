package com.center.aurora.service.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUserDto {
    private Long id;
    private String name;
    private String avatar;

    @Builder
    public PostUserDto(Long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }
}