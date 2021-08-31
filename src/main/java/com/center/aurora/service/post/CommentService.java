package com.center.aurora.service.post;

import com.center.aurora.domain.post.Comment;
import com.center.aurora.domain.post.Post;
import com.center.aurora.domain.user.User;
import com.center.aurora.exception.UserAuthException;
import com.center.aurora.repository.post.CommentRepository;
import com.center.aurora.repository.post.PostRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.post.dto.CommentDto;

import com.center.aurora.service.post.dto.CommentResponse;
import com.center.aurora.service.post.dto.PostUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Map getComment(Long post_id){
        Post post = postRepository.findById(post_id).get();
        Map result = new HashMap();

        List<Comment> list = commentRepository.findByPostOrderByIdDesc(post);
        List<CommentResponse> comments = new ArrayList<>();

        for(Comment comment : list){
            PostUserDto postUserDto = PostUserDto.builder()
                    .id(comment.getWriter().getId())
                    .name(comment.getWriter().getName())
                    .avatar(comment.getWriter().getImage())
                    .build();

            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .auth(postUserDto)
                    .content(comment.getContent())
                    .build();
            comments.add(commentResponse);
        }

        int commentCnt = commentRepository.findByPostOrderByIdDesc(post).size();

        result.put("comments", comments);
        result.put("commentCnt", commentCnt);
        return result;
    }

    @Transactional
    public void createComment(Long user_id, Long post_id, CommentDto commentDto){
        User user = userRepository.findById(user_id).get();
        Post post = postRepository.findById(post_id).get();
        Comment comment = Comment.builder()
                .writer(user)
                .post(post)
                .content(commentDto.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long user_id, Long comment_id, CommentDto commentDto){
        Comment comment = commentRepository.findById(comment_id).get();
        if(comment.getWriter().getId() == user_id){
            comment.update(commentDto.getContent());
        }else{
            throw new UserAuthException("유저 권한이 없습니다.");
        }
    }

    @Transactional
    public void deleteComment(Long user_id, Long comment_id){
        Comment comment = commentRepository.findById(comment_id).get();
        if(comment.getWriter().getId() == user_id){
            commentRepository.deleteById(comment_id);
        }else{
            throw new UserAuthException("유저 권한이 없습니다.");
        }
    }

}
