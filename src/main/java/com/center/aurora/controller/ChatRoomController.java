package com.center.aurora.controller;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import com.center.aurora.service.chat.ChatRoomService;
import com.center.aurora.service.chat.dto.ChatRoomDto;
import com.center.aurora.service.chat.dto.ChatRoomListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public List<ChatRoomListDto> room(@CurrentUser UserPrincipal user) throws Exception{
        return chatRoomService.findAllByUserId(user.getId());
    }

    @PostMapping("/room/{friendId}")
    public void createRoom(@CurrentUser UserPrincipal user, @PathVariable Long friendId){
        log.info(String.valueOf(friendId));
        chatRoomService.createChatRoom(user.getId(), friendId);
    }

    @GetMapping("/room/{roomId}")
    public ChatRoomDto roomInfo(@CurrentUser UserPrincipal user, @PathVariable Long roomId) throws Exception{
        return chatRoomService.findRoomById(roomId, user.getId());
    }

    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) throws IllegalAccessException {
        chatRoomService.deleteRoomById(roomId);
    }


}
