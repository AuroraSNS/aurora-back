package com.center.aurora.service.user.dto;

import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import lombok.Getter;

@Getter
public class FriendListDto {
    Long id;
    String name;
    String bio;

    public FriendListDto(Long id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }
    public static FriendListDto entityToDto(Friend friend){
        User myFriend = friend.getYou();
        return new FriendListDto(myFriend.getId(), myFriend.getName(), myFriend.getBio());
    }
}
