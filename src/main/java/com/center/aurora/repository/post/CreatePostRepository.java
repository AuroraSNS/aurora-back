package com.center.aurora.repository.post;

import com.center.aurora.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatePostRepository extends JpaRepository<Post, Long>{
}
