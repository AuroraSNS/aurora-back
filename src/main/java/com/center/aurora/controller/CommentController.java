package com.center.aurora.controller;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.comment.CommentService;
import com.center.aurora.service.comment.dto.CommentDto;
import com.center.aurora.service.comment.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping ("/comments/{postId}")
    public Map getComment(@PathVariable("postId") Long post_id){
        return commentService.getComment(post_id);
    }

    @PostMapping("/comments/{postId}")
    public void createComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id,@RequestBody CommentDto commentDto){
        commentService.createComment(userPrincipal.getId(), post_id, commentDto);
    }

    @PatchMapping("/comments/{commentId}")
    public void updateComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("commentId") Long comment_id, @RequestBody CommentDto commentDto){
        commentService.updateComment(userPrincipal.getId(), comment_id, commentDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("commentId") Long comment_id){
        commentService.deleteComment(userPrincipal.getId(), comment_id);
    }
}
