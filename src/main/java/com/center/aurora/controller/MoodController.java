package com.center.aurora.controller;

import com.center.aurora.service.mood.MoodService;
import com.center.aurora.service.mood.dto.MoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MoodController {
    private final MoodService moodService;

    @GetMapping("/mood/all")
    public MoodResponse getAllMood(){
        return moodService.getAllMood();
    }

    @GetMapping("/mood/{userId}")
    public MoodResponse getMoodByUser(@PathVariable("userId") Long user_id){
        return moodService.getMoodByUser(user_id);
    }
}
