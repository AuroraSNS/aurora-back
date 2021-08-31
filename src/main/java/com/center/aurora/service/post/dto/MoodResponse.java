package com.center.aurora.service.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MoodResponse {
    private int sun;
    private int cloud;
    private int rain;
    private int moon;

    @Builder
    public MoodResponse(int sun, int cloud, int rain, int moon) {
        this.sun = sun;
        this.cloud = cloud;
        this.rain = rain;
        this.moon = moon;
    }
}
