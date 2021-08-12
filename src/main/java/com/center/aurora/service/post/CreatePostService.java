package com.center.aurora.service.post;


import com.center.aurora.utils.S3Uploader;
import com.center.aurora.domain.post.Image;
import com.center.aurora.repository.post.ImageRepository;
import com.center.aurora.service.post.dto.CreatePostRequest;
import com.center.aurora.service.post.dto.CreatePostResponse;
import com.center.aurora.domain.post.Post;
import com.center.aurora.repository.post.CreatePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreatePostService {
    private final CreatePostRepository postRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    @Transactional
    public CreatePostRequest createPost(CreatePostResponse postDto) throws IOException {
        ArrayList<String> images = new ArrayList<>();

        for (MultipartFile iamge : postDto.getImage()) {
            images.add(s3Uploader.upload(iamge, "aurora"));
        }
        Post post = Post.builder()
                .writer(postDto.getWriter())
                .mood(postDto.getMood())
                .content(postDto.getContent())
                .build();
        Long post_id = postRepository.save(post).getId();

        CreatePostRequest dto = CreatePostRequest.builder()
                .writer(postDto.getWriter())
                .mood(postDto.getMood())
                .content(postDto.getContent())
                .image(images)
                .build();

        for (String imageValue : images) {
            Image image = Image.builder()
                    .post_id(post_id)
                    .image(imageValue)
                    .build();

            imageRepository.save(image);
        }


        return dto;
    }
}
