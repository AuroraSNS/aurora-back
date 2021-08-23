package com.center.aurora.repository.comment;

import com.center.aurora.domain.comment.Comment;
import com.center.aurora.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPost(Pageable pageable,Post post);
}
