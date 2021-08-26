package com.center.aurora.service.like;

import com.center.aurora.domain.like.Like;
import com.center.aurora.domain.like.LikeId;
import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import com.center.aurora.exception.UserAuthException;
import com.center.aurora.repository.like.LikeRepository;
import com.center.aurora.repository.post.PostRepository;
import com.center.aurora.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void createLike(Long user_id, Long post_id){
        User user = userRepository.findById(user_id).get();
        Post post = postRepository.findById(post_id).get();
        LikeId likeId = new LikeId(user.getId(), post.getId());
        Like like = Like.builder().post(post).writer(user).id(likeId).build();
        likeRepository.save(like);
    }

    @Transactional
    public void deleteLike(Long user_id, Long post_id){
        User user = userRepository.findById(user_id).get();
        Post post = postRepository.findById(post_id).get();
        Like like = likeRepository.findByPostAndWriter(post,user);
        if(like != null){
            likeRepository.delete(like);
        }else{
            throw new UserAuthException("유저 권한이 없습니다.");
        }
    }
}
