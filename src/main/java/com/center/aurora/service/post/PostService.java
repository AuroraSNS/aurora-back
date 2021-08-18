package com.center.aurora.service.post;


import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.post.dto.PostListResponse;
import com.center.aurora.service.post.dto.PostListUserDto;
import com.center.aurora.utils.S3Uploader;
import com.center.aurora.domain.post.Image;
import com.center.aurora.repository.post.ImageRepository;
import com.center.aurora.service.post.dto.PostCreateResponseDto;
import com.center.aurora.service.post.dto.PostCreateRequestDto;
import com.center.aurora.domain.post.Post;
import com.center.aurora.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public PostCreateResponseDto createPost(Long id, PostCreateRequestDto postDto) throws IOException {
        User user = userRepository.findById(id).get();
        Post post = Post.builder()
                .writer(user)
                .mood(postDto.getMood())
                .content(postDto.getContent())
                .build();
        Long postId = postRepository.save(post).getId();

        List<String> images = new ArrayList<>();

        if(postDto.getImage()!=null){
            for (MultipartFile imageValue : postDto.getImage()) {
                images.add(s3Uploader.upload(imageValue, "aurora"));
            }
        }

        for (String imageValue : images) {
            Image image = Image.builder()
                    .post_id(postId)
                    .image(imageValue)
                    .build();

            imageRepository.save(image);
        }

        PostCreateResponseDto dto = PostCreateResponseDto.builder()
                .writer(id)
                .mood(postDto.getMood())
                .content(postDto.getContent())
                .image(images)
                .build();

        return dto;
    }
    @Transactional
    public List<PostListResponse> getAllPost(Pageable pageable) {
        List<PostListResponse> posts = new ArrayList<>();

        Page<Post> list = postRepository.findAll(pageable);
        for (Post post : list.getContent()){

            PostListUserDto getAllPostUser = PostListUserDto.builder()
                    .id(post.getWriter().getId())
                    .name(post.getWriter().getName())
                    .avatar(post.getWriter().getImage())
                    .build();

            List<String> images = imageRepository.findAllImageByPostId(post.getId());


            PostListResponse getAllPostResponse = PostListResponse.builder()
                    .id(post.getId())
                    .getAllPostUser(getAllPostUser)
                    .mood(post.getMood())
                    .content(post.getContent())
                    .images(images)
                    .build();
            posts.add(getAllPostResponse);
        }

        return posts;
    }
}
