package com.center.aurora.controller;

import com.center.aurora.domain.chat.Message;
import com.center.aurora.repository.chat.MessageRepository;
import com.center.aurora.security.TokenProvider;
import com.center.aurora.repository.chat.ChatRoomRepository;
import com.center.aurora.service.chat.MessageService;
import com.center.aurora.service.chat.dto.MessageRecvDto;
import com.center.aurora.service.chat.dto.MessageSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final MessageService messageService;

    private final TokenProvider tokenProvider;

    @MessageMapping("/chat/message")
    public void message(MessageRecvDto message, @Header("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);

        log.info("Room ID = " + message.getRoomId() + " message = " + message.getMessage());
        MessageSendDto sendMessage = messageService.saveMessage(message, userId);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), sendMessage);

    }


}
