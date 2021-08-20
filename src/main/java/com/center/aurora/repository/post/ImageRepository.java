package com.center.aurora.repository.post;

import com.center.aurora.domain.post.Image;
import com.center.aurora.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>{
    @Query("Select distinct i.image From Image i where i.post = :post")
    List<String> findAllImageByPostId(@Param("post") Post post);

    @Modifying
    @Transactional
    @Query("Delete From Image i where i.post = :post")
    void deleteAllByPostId(@Param("post") Post post);
}
