package com.center.aurora.controller;

import com.center.aurora.config.AppProperties;
import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.Friend;
import com.center.aurora.domain.user.friend.FriendId;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.user.FriendService;
import com.center.aurora.service.user.dto.FriendListDto;
import com.center.aurora.utils.CookieUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenProvider tokenProvider;

    private MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();
        friendRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 모든_친구_조회() throws Exception{
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .bio("A")
                .image("")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("B")
                .email("B@B.com")
                .bio("B")
                .image("")
                .role(Role.USER)
                .build();
        User userC = User.builder()
                .name("C")
                .email("B@B.com")
                .bio("C")
                .image("")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        friendService.addFriend(userA.getId(), userB.getId());
        friendService.addFriend(userA.getId(), userC.getId());

        String url = "http://localhost:" + port + "/friend";
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        //when
        MvcResult result = mvc.perform(get(url).cookie(accessToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }


    @Test
    public void 친구등록() throws Exception{
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .bio("A")
                .image("")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("B")
                .email("B@B.com")
                .bio("B")
                .image("")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);

        String url = "http://localhost:" + port + "/friend/" + userB.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);
        //when
        mvc.perform(post(url).cookie(accessToken)).andExpect(status().isOk());
        //then
        List<FriendListDto> allFriends = friendService.findAllFriends(userA.getId());

        FriendListDto myFriend = allFriends.get(0);
        assertThat(myFriend.getId()).isEqualTo(userB.getId());
        assertThat(myFriend.getName()).isEqualTo(userB.getName());

    }
    @Test
    public void 친구삭제() throws Exception {
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .bio("A")
                .image("")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("B")
                .email("B@B.com")
                .bio("B")
                .image("")
                .role(Role.USER)
                .build();
        User userC = User.builder()
                .name("C")
                .email("B@B.com")
                .bio("C")
                .image("")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);

        friendService.addFriend(userA.getId(), userB.getId());
        friendService.addFriend(userA.getId(), userC.getId());

        String url = "http://localhost:" + port + "/friend/" + userB.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        Cookie accessToken = new Cookie(CookieUtils.ACCESS_TOKEN_NAME, token);

        //when
        mvc.perform(delete(url).cookie(accessToken))
                .andExpect(status().isOk());
        //then
        List<FriendListDto> allFriends = friendService.findAllFriends(userA.getId());
        FriendListDto leftFriend = allFriends.get(0);
        assertThat(allFriends.size()).isEqualTo(1);
        assertThat(leftFriend.getName()).isEqualTo(userC.getName());
        assertThat(leftFriend.getId()).isEqualTo(userC.getId());
    }
}