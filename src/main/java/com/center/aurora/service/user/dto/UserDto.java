package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String avatar;
    private String bio;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getImage();
        this.bio = user.getBio();
    }
}
