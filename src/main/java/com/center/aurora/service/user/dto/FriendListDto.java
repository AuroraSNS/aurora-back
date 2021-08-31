package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import lombok.Getter;

@Getter
public class FriendListDto {
    Long id;
    String name;
    String avatar;

    public FriendListDto(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.avatar = image;
    }
    public static FriendListDto entityToDto(User myFriend){

        return new FriendListDto(myFriend.getId(), myFriend.getName(), myFriend.getImage());
    }
}
