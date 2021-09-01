package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;

@Getter
public class FriendListDto {
    Long id;
    String name;
    String avatar;

    public FriendListDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getImage();
    }
}
