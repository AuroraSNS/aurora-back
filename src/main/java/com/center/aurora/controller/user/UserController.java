package com.center.aurora.controller.user;

import com.center.aurora.domain.user.User;
import com.center.aurora.domain.user.friend.FriendStatus;
import com.center.aurora.repository.post.LikeRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.user.UserService;
import com.center.aurora.service.user.dto.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;

    @GetMapping("")
    @ApiOperation(value = "내 정보 조회", notes = "내정보를 조회합니다.")
    @ApiResponse(code = 200, message = "success", response = UserMeDto.class)
    public UserMeDto getCurrentUser(@CurrentUser UserPrincipal user) {
        User me = userRepository.findById(user.getId()).get();
        List<Long> likeList = likeRepository.findAllPostIdByWriter(me);
        return new UserMeDto(me,likeList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "특정 유저 조회", notes = "ID를 이용해 유저를 조회합니다.")
    @ApiImplicitParam(name = "id", value = "유저 Id값", dataType = "Long", paramType = "path")
    public UserDto getUser(@CurrentUser UserPrincipal user, @PathVariable Long id){
        if(user == null){
            User user1 = userRepository.findById(id).get();
            return new UserDto(user1, "NEED_LOGIN");
        }else{
            return userService.getUser(user.getId(), id);
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "유저 검색", notes = "이름을 기준으로 유저를 조회합니다.")
    public List<UserListDto> findUsersByName(@RequestParam String name){
        return userService.findUsersByName(name);
    }

    @PatchMapping(value = "")
    @ApiOperation(value = "유저 정보 업데이트", notes = "유저 정보를 업데이트 합니다. (이름, 상태메시지, 사진)")
    public void updateUser(@CurrentUser UserPrincipal user,
//                           @ModelAttribute UserUpdateDto updateDto
                            @RequestParam(value = "image", required = false) MultipartFile image,
                            @RequestParam(value = "name") String name,
                            @RequestParam(value = "bio", required = false) String bio,
                            @RequestParam(value = "isImageChanged") Boolean isImageChanged
    ) throws IOException {

        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name(name)
                .image(image)
                .bio(bio)
                .isImageChanged(isImageChanged)
                .build();

        log.info("수정할 데이터 : " + updateDto);
        userService.userUpdate(user.getId(), updateDto);
    }
}
