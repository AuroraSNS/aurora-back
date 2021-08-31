package com.center.aurora.repository.post;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByWriter(Pageable pageable, User writer);

    @Query("Select p.mood From Post p")
    List<Mood> findAllMood();

    @Query("Select p.mood From Post p where p.writer = :writer")
    List<Mood> findAllMoodByUser(@Param("writer") User writer);
}