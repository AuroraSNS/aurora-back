package com.center.aurora.service.chat.dto;

import com.center.aurora.domain.chat.ChatRoom;
import com.center.aurora.domain.chat.Message;
import com.center.aurora.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomListDto {

    public Long roomId;
    public Usr user;
    public String lastMessage;
    public LocalDateTime lastTimeStamp;

    public ChatRoomListDto(Long meId, ChatRoom room, Message message){
        boolean p1isMe = room.getParticipant1().getId() == meId;
        this.roomId = room.getId();
        this.user = new Usr((p1isMe)? room.getParticipant2() : room.getParticipant1());
        if(message == null) {
            this.lastMessage = "";
            this.lastTimeStamp = null;
        }
        else {
            this.lastMessage = message.getMessage();
            this.lastTimeStamp = message.getTimeStamp();
        }
    }

    class Usr {
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
