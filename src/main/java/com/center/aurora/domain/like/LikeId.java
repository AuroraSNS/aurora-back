package com.center.aurora.domain.like;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
@Data
public class LikeId implements Serializable {
    private Long post;
    private Long user;

    public LikeId(Long post, Long user) {
        this.post = post;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(getPost(), likeId.getPost()) && Objects.equals(getUser(), likeId.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPost(), getUser());
    }
}
