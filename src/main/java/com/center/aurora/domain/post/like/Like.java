package com.center.aurora.domain.post.like;

import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name="Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @EmbeddedId
    private LikeId id;

    @MapsId("post")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Builder
    public Like(LikeId id, Post post, User writer) {
        this.id = id;
        this.post = post;
        this.writer = writer;
    }
}
