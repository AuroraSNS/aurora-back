package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;

@Getter
public class UserListDto {
    private Long id;

    private String name;

    private String avatar;

    public UserListDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getImage();
    }
}
