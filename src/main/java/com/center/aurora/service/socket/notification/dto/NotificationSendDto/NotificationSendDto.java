package com.center.aurora.service.socket.notification.dto.NotificationSendDto;

import com.center.aurora.domain.user.User;
import lombok.*;

@ToString
@Setter
@Getter
public abstract class NotificationSendDto {

    private String type;

    public NotificationSendDto(String type) {
        this.type = type;
    }

}
