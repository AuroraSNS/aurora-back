package com.center.aurora.service.post;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.post.PostRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.post.dto.MoodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public MoodResponse getAllMood(){
        List<Mood> moods = postRepository.findAllMood();

        return fetchMood(moods);
    }

    @Transactional
    public MoodResponse getMoodByUser(Long user_id){
        User user = userRepository.findById(user_id).get();
        List<Mood> moods = postRepository.findAllMoodByUser(user);

        return fetchMood(moods);
    }



    public MoodResponse fetchMood(List<Mood> moods){
        double sun = 0;
        double cloud = 0;
        double rain = 0;
        double moon = 0;

        for(Mood mood : moods){
            if(mood == Mood.sun) sun++;
            else if(mood == Mood.cloud) cloud++;
            else if(mood == Mood.rain) rain++;
            else moon++;
        }

        double total = sun + cloud + rain + moon;

        if(total!=0){
            sun = sun/total*100;
            cloud = cloud/total*100;
            rain = rain/total*100;
            moon = moon/total*100;
        }

        MoodResponse moodResponse = MoodResponse.builder()
                .sun((int)sun)
                .rain((int)rain)
                .cloud((int)cloud)
                .moon((int)moon)
                .build();

        return moodResponse;
    }
}
