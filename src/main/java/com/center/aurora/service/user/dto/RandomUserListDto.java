package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RandomUserListDto {

    Long id;

    String name;

    String avatar;

    public RandomUserListDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getImage();
    }
}
