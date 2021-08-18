package com.center.aurora.domain.user.friend;

import com.center.aurora.domain.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
@Data
public class FriendId implements Serializable {
    private Long me;
    private Long you;

    public FriendId(Long me, Long you) {
        this.me = me;
        this.you = you;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(getMe(), friendId.getMe()) && Objects.equals(getYou(), friendId.getYou());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMe(), getYou());
    }
}
