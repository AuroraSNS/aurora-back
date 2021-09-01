package com.center.aurora.service.user;

import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.domain.user.friend.FriendStatus;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.user.dto.UserDto;
import com.center.aurora.service.user.dto.UserListDto;
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

    @Autowired
    private FriendRepository friendRepository;

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
        List<UserListDto> Friends = userService.findUsersByName("A");

        //then
        assertThat(Friends.size()).isEqualTo(3);
        assertThat(Friends.get(0).getName()).isEqualTo("A");
    }

    @Test
    public void 특정_유저_정보및_친구관계까지_받아온다() throws Exception{
        //given
        User userA = User.builder()
                .name("B")
                .email("B@B.com")
                .bio("B")
                .image("")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("AA")
                .email("AA@AA.com")
                .bio("AA")
                .image("")
                .role(Role.USER)
                .build();
        User userC = User.builder()
                .name("AAC")
                .email("AAC@AAC.com")
                .bio("AAC")
                .image("")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        Friend friendAB = Friend.builder().id(new FriendId(userA.getId(), userB.getId())).me(userA).you(userB).status(FriendStatus.FRIEND).build();
        Friend friendBA = Friend.builder().id(new FriendId(userB.getId(), userA.getId())).you(userA).me(userB).status(FriendStatus.FRIEND).build();

        Friend friendAC = Friend.builder().id(new FriendId(userA.getId(), userC.getId())).me(userA).you(userC).status(FriendStatus.ONGOING).build();
        Friend friendCA = Friend.builder().id(new FriendId(userC.getId(), userA.getId())).you(userA).me(userC).status(FriendStatus.ONGOING).build();

        friendRepository.save(friendAB);
        friendRepository.save(friendBA);
        friendRepository.save(friendAC);
        friendRepository.save(friendCA);
        //when
        UserDto fromAToB = userService.getUser(userA.getId(), userB.getId());
        UserDto fromAToC = userService.getUser(userA.getId(), userC.getId());

        UserDto fromBToA = userService.getUser(userB.getId(), userA.getId());
        UserDto fromBToC = userService.getUser(userB.getId(), userC.getId());
        //then

        assertThat(fromAToB.getStatus()).isEqualTo(FriendStatus.FRIEND.name());
        assertThat(fromAToC.getStatus()).isEqualTo(FriendStatus.ONGOING.name());
        assertThat(fromBToA.getStatus()).isEqualTo(FriendStatus.FRIEND.name());
        assertThat(fromBToC.getStatus()).isEqualTo(FriendStatus.NOT_FRIEND.name());
    }
}
