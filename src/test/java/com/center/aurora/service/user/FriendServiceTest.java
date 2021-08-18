package com.center.aurora.service.user;

import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.FriendListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendServiceTest {
    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void dbCleanUp() {
        friendRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 친구_등록_후_모든_친구조회(){
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        User userC = User.builder().name("C").email("c@c.com").image("").role(Role.USER).bio("").build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        //when
        friendService.addFriend(userA.getId(), userB.getId());
        friendService.addFriend(userA.getId(), userC.getId());

        //then
        List<FriendListDto> aFriends = friendService.findAllFriends(userA.getId());
        List<FriendListDto> bFriends = friendService.findAllFriends(userB.getId());
        List<FriendListDto> cFriends = friendService.findAllFriends(userC.getId());

        assertThat(aFriends.size()).isEqualTo(2);
        assertThat(bFriends.size()).isEqualTo(1);
        assertThat(bFriends.get(0).getName()).isEqualTo("A");
        assertThat(cFriends.size()).isEqualTo(1);
        assertThat(cFriends.get(0).getName()).isEqualTo("A");
    }

    @Test
    public void 친구_삭제(){
        //given
        User userA = User.builder().name("A").email("a@a.com").image("").role(Role.USER).bio("").build();
        User userB = User.builder().name("B").email("b@b.com").image("").role(Role.USER).bio("").build();
        User userC = User.builder().name("C").email("c@c.com").image("").role(Role.USER).bio("").build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        friendService.addFriend(userA.getId(), userB.getId());
        friendService.addFriend(userA.getId(), userC.getId());
        //when

        friendService.deleteFriend(userA.getId(), userC.getId());
        //then
        List<FriendListDto> aFriends = friendService.findAllFriends(userA.getId());
        List<FriendListDto> bFriends = friendService.findAllFriends(userB.getId());
        List<FriendListDto> cFriends = friendService.findAllFriends(userC.getId());

        assertThat(aFriends.size()).isEqualTo(1);
        assertThat(bFriends.size()).isEqualTo(1);
        assertThat(bFriends.get(0).getName()).isEqualTo("A");
        assertThat(cFriends.size()).isEqualTo(0);
    }
}