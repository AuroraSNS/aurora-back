package com.center.aurora.controller;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/likes/{postId}")
    public void createLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id){
        likeService.createLike(userPrincipal.getId(), post_id);
    }

    @DeleteMapping("/likes/{postId}")
    public void deleteLike(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long post_id){
        likeService.deleteLike(userPrincipal.getId(), post_id);
    }
}
