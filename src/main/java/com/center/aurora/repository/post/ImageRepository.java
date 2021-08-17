package com.center.aurora.repository.post;

import com.center.aurora.domain.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>{
    @Query("Select distinct i.image From Image i where i.post_id = :postId")
    List<String> findAllImageByPostId(Long postId);
}
