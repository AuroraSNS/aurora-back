package com.center.aurora.service.notification.dto.NotificationSendDto;

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
