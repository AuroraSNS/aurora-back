package com.center.aurora.service.chat.dto;

import com.center.aurora.domain.chat.Message;
import com.center.aurora.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

public class MessageSendDto {
    public Long id;
    public Usr sender;
    public String message;
    public LocalDateTime timeStamp;

    public MessageSendDto(Message msg) {
        this.id = msg.getId();
        this.sender = new Usr(msg.getSender());
        this.message = msg.getMessage();
        this.timeStamp = msg.getTimeStamp();
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
