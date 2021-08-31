package com.center.aurora.service.chat.dto;

import com.center.aurora.domain.chat.ChatRoom;
import com.center.aurora.domain.chat.Message;
import com.center.aurora.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomDto {

    public List<Msg> messages;

    public ChatRoomDto(ChatRoom room) {
        this.messages = room.getMessages().stream()
                .map(Msg::new)
                .collect(Collectors.toList());
    }

    class Msg{
        public Long id;
        public Usr sender;
        public String message;
        public LocalDateTime timeStamp;

        public Msg(Message message) {
            this.id = message.getId();
            this.sender = new Usr(message.getSender());
            this.message = message.getMessage();
            this.timeStamp = message.getTimeStamp();
        }
    }

    @Getter
    class Usr{
        public Long id;
        public String name;
        public String avatar;

        public Usr(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.avatar = user.getImage();
        }
    }
}
