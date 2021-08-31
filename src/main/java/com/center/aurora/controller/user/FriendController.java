package com.center.aurora.controller.user;

import com.center.aurora.domain.post.Mood;
import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.user.FriendService;
import com.center.aurora.service.user.dto.FriendListDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/friend")
@RestController
public class FriendController {

    private final FriendService friendService;

    @GetMapping("")
    @ApiOperation(value = "친구 목록 전체 조회", notes = "현재 사용자의 모든 친구 목록을 반환합니다.")
    public List<FriendListDto> findAllFriends(@CurrentUser UserPrincipal user){
        return friendService.findAllFriends(user.getId());
    }

    @PostMapping("/{friendId}")
    @ApiOperation(value = "친구 추가", notes = "현재 사용자가 id에 해당하는 유저를 친구 목록에 추가합니다.")
    @ApiImplicitParam(name = "friendId", value = "친구 Id값", dataType = "Long", paramType = "path")
    public void addFriend(@CurrentUser UserPrincipal user, @PathVariable Long friendId){
        friendService.addFriend(user.getId(), friendId);
    }

    @DeleteMapping("/{friendId}")
    @ApiOperation(value = "친구 삭제", notes = "현재 사용자의 친구 목록에서 id에 해당하는 유저를 삭제합니다.")
    @ApiImplicitParam(name = "friendId", value = "친구 Id값", dataType = "Long", paramType = "path")
    public void deleteFriend(@CurrentUser UserPrincipal user, @PathVariable Long friendId){
        friendService.deleteFriend(user.getId(), friendId);
    }
}
