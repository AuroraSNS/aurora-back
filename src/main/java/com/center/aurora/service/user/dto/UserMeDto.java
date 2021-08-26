package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserMeDto {

    private Long id;
    private String email;
    private String name;
    private String avatar;
    private String bio;
    private List<Long> likeList;

    public UserMeDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatar = user.getImage();
        this.bio = user.getBio();
    }

    public UserMeDto(User user, List<Long> likeList) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatar = user.getImage();
        this.bio = user.getBio();
        this.likeList = likeList;
    }
}
