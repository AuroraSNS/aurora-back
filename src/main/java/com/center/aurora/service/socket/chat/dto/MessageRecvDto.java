package com.center.aurora.service.socket.chat.dto;

import lombok.Getter;

@Getter
public class MessageRecvDto {

    public Long roomId;
    public String message;
}
