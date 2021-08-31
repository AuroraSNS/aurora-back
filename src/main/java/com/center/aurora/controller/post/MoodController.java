package com.center.aurora.controller.post;

import com.center.aurora.service.post.MoodService;
import com.center.aurora.service.post.dto.MoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mood")
@RequiredArgsConstructor
public class MoodController {
    private final MoodService moodService;

    @GetMapping("/all")
    public MoodResponse getAllMood(){
        return moodService.getAllMood();
    }

    @GetMapping("/{userId}")
    public MoodResponse getMoodByUser(@PathVariable("userId") Long user_id){
        return moodService.getMoodByUser(user_id);
    }
}
