package com.center.aurora.domain.user.friend;

import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Friend {

    @EmbeddedId
    private FriendId id;

    @MapsId("me")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "me_id")
    private User me;

    @MapsId("you")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "you_id")
    private User you;

    @Builder
    public Friend(FriendId id, User me, User you) {
        this.id = id;
        this.me = me;
        this.you = you;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", me=" + me +
                ", you=" + you +
                '}';
    }
}
