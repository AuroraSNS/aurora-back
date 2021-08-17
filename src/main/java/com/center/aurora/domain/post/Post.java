package com.center.aurora.domain.post;

import com.center.aurora.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer") // => FK PK
    private User writer;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // VARCHAR(255)

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Builder
    public Post(User writer, Mood mood, String content) {
        this.writer = writer;
        this.mood = mood;
        this.content = content;
    }

}
