package com.center.aurora.repository.comment;

import com.center.aurora.domain.comment.Comment;
import com.center.aurora.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByIdDesc(Post post);
}
