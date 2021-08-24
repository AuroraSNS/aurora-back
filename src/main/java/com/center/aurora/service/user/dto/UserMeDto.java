package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserMeDto {

    private Long id;
    private String email;
    private String name;
    private String avatar;
    private String bio;

    public UserMeDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatar = user.getImage();
        this.bio = user.getBio();
    }
}
