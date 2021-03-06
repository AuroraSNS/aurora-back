package com.center.aurora.controller.user;

import com.center.aurora.domain.user.Role;
import com.center.aurora.domain.user.User;
import com.center.aurora.repository.user.FriendRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

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
        userRepository.deleteAll();
    }

    @Test
    public void 내_정보를_받아온다() throws Exception {
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("A입니다.")
                .role(Role.USER)
                .build();

        userRepository.save(userA);

        String url = "http://localhost:" + port + "/user";
        String token = tokenProvider.createTokenByUserEntity(userA);
        //when
        MvcResult res = mvc.perform(get(url).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //then
        String contentAsString = res.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }
    @Test
    public void 특정_유저_정보를_받아온다() throws Exception{
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("A입니다.")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("B")
                .email("B@B.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("B입니다.")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);

        String url = "http://localhost:"+ port + "/user/" + userB.getId();
        //when
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString.contains(userB.getBio())).isTrue();
    }
    @Test
    public void 특정_유저_정보를_받아온다_ACCESTOKEN과_함께() throws Exception{
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("A입니다.")
                .role(Role.USER)
                .build();
        User userB = User.builder()
                .name("B")
                .email("B@B.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("B입니다.")
                .role(Role.USER)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);

        String url = "http://localhost:"+ port + "/user/" + userB.getId();
        String token = tokenProvider.createTokenByUserEntity(userA);
        //when
        MvcResult mvcResult = mvc.perform(get(url).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString.contains(userB.getBio())).isTrue();
        assertThat(contentAsString.contains("NOT_FRIEND")).isTrue();
    }

    @Test
    public void 친구검색() throws Exception{
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

        String url = "http://localhost:" + port + "/user/search?name=AA";

        //when
        MvcResult result = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void 유저를_업데이트_한다_이미지는_안한다() throws Exception{
        //given
        User userA = User.builder()
                .name("A")
                .email("A@A.com")
                .image(User.DEFAULT_IMAGE_URL)
                .bio("A입니다.")
                .role(Role.USER)
                .build();

        userRepository.save(userA);

        String url = "http://localhost:"+ port + "/user";
        String token = tokenProvider.createTokenByUserEntity(userA);

        //when
        String updateName = "AA";
        String updateBio = "사실 AA입니다.";
        Boolean updateIsImageChanged = false;

        mvc.perform(patch(url)
                .param("name", updateName)
                .param("bio", updateBio)
                .param("isImageChanged", "false")
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
        //then

        User updatedUser = userRepository.findById(userA.getId()).get();
        assertThat(updatedUser.getName()).isEqualTo(updateName);
        assertThat(updatedUser.getBio()).isEqualTo(updateBio);

    }


}