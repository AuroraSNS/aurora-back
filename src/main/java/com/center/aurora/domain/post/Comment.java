package com.center.aurora.domain.post;

import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public Comment(User writer, Post post, String content) {
        this.writer = writer;
        this.post = post;
        this.content = content;
    }

    public void update(String content){
        this.content = content;
    }
}
