package com.center.aurora.service.socket.chat;

import com.center.aurora.domain.chat.Message;
import com.center.aurora.repository.socket.ChatRoomRepository;
import com.center.aurora.repository.socket.MessageRepository;
import com.center.aurora.repository.user.UserRepository;
import com.center.aurora.service.socket.chat.dto.MessageRecvDto;
import com.center.aurora.service.socket.chat.dto.MessageSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public MessageSendDto saveMessage(MessageRecvDto message, Long ownerId) {
        Message msg = Message.builder()
                .message(message.getMessage())
                .sender(userRepository.findById(ownerId).get())
                .room(chatRoomRepository.findById(message.getRoomId()).get())
                .build();
        messageRepository.save(msg);
        return new MessageSendDto(msg);
    }
}
