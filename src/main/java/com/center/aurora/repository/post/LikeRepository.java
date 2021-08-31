package com.center.aurora.repository.post;

import com.center.aurora.domain.post.like.Like;
import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("Select l.post.id From Like l where l.writer = :writer")
    List<Long> findAllPostIdByWriter(@Param("writer") User writer);

    Like findByPostAndWriter(Post post, User writer);

    @Query("Select count(l) From Like l where l.post = :post")
    int findAllByPost(@Param("post") Post post);
}
