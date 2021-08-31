package com.center.aurora.controller.post;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.post.CommentService;
import com.center.aurora.service.post.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping ("/{postId}")
    public Map getComment(@PathVariable("postId") Long post_id){
        return commentService.getComment(post_id);
    }

    @PostMapping("/{postId}")
    public void createComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id,@RequestBody CommentDto commentDto){
        commentService.createComment(userPrincipal.getId(), post_id, commentDto);
    }

    @PatchMapping("/{commentId}")
    public void updateComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("commentId") Long comment_id, @RequestBody CommentDto commentDto){
        commentService.updateComment(userPrincipal.getId(), comment_id, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("commentId") Long comment_id){
        commentService.deleteComment(userPrincipal.getId(), comment_id);
    }
}
