package com.center.aurora.service.user;

import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void 친구검색(){
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("AA").email("b@b.com").image("").role(Role.USER).bio("").build();
        User userC = User.builder().name("AAA").email("c@c.com").image("").role(Role.USER).bio("").build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        //when
        List<FriendListDto> Friends = userService.findFriendsByName("A");

        //then
        assertThat(Friends.size()).isEqualTo(3);
        assertThat(Friends.get(0).getName()).isEqualTo("A");
    }
}
