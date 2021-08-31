package com.center.aurora.controller;

import com.center.aurora.security.TokenProvider;
import com.center.aurora.service.socket.chat.MessageService;
import com.center.aurora.service.socket.chat.dto.MessageRecvDto;
import com.center.aurora.service.socket.chat.dto.MessageSendDto;
import com.center.aurora.service.socket.notification.NotificationService;
import com.center.aurora.service.socket.notification.dto.NotificationRecvDto;
import com.center.aurora.service.socket.notification.dto.NotificationSendDto.NotificationSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final MessageService messageService;

    private final NotificationService notificationService;

    private final TokenProvider tokenProvider;

    @MessageMapping("/chat/message")
    public void message(MessageRecvDto message, @Header("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);

        log.info("Room ID = " + message.getRoomId() + " message = " + message.getMessage());
        MessageSendDto sendMessage = messageService.saveMessage(message, userId);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), sendMessage);
    }

    @MessageMapping("/notification")
    public void notification(NotificationRecvDto notificationRecvDto, @Header("Authorization") String token){
        Long userId = tokenProvider.getUserIdFromToken(token);

        log.info("전송 받은 알림 : " + notificationRecvDto);
        NotificationSendDto notification = notificationService.saveNotification(notificationRecvDto);
        log.info("돌려줄 Topic : " + "/sub/notification/" + notificationRecvDto.getTo());
        log.info("돌려주는 정보 : " + notification);
        messagingTemplate.convertAndSend("/sub/notification/" + notificationRecvDto.getTo(), notification);
    }



}
