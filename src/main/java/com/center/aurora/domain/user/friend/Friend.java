package com.center.aurora.domain.user.friend;

import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Friend {

    @EmbeddedId
    private FriendId id;

    @MapsId("me")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User me;

    @MapsId("you")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User you;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public void changeStatusToFriend(){
        this.status = FriendStatus.FRIEND;
    }

    @Builder
    public Friend(FriendId id, User me, User you, FriendStatus status) {
        this.id = id;
        this.me = me;
        this.you = you;
        this.status = status;
    }
}
